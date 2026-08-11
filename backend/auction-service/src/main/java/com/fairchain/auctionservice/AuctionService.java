package com.fairchain.auctionservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final AiClient aiClient;
    private final KafkaTemplate<String, SaleConfirmedEvent> kafkaTemplate;
    private final String saleConfirmedTopic;

    public AuctionService(AuctionRepository auctionRepository,
                           BidRepository bidRepository,
                           AiClient aiClient,
                           KafkaTemplate<String, SaleConfirmedEvent> kafkaTemplate,
                           @Value("${fairchain.kafka.topic.sale-confirmed}") String saleConfirmedTopic) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.aiClient = aiClient;
        this.kafkaTemplate = kafkaTemplate;
        this.saleConfirmedTopic = saleConfirmedTopic;
    }

    /**
     * Opens an auction when a batch is registered. INFERENCE, not
     * explicit in the spec: Section 1.4 steps 2-3 imply this ordering
     * (AI computes band -> auction opens) but never states what event
     * triggers auction creation. Flagged.
     */
    @KafkaListener(topics = "${fairchain.kafka.topic.batch-registered}", groupId = "auction-service")
    public void onBatchRegistered(BatchRegisteredEvent event) {
        FairPriceBandResponse band = aiClient.getFairPriceBand(event.getCrop(), event.getRegion());

        Auction auction = new Auction(event.getBatchId(), "OPEN", band.getMinPrice());
        auctionRepository.save(auction);
    }

    public BidResponse placeBid(String batchId, BidRequest request) {
        Auction auction = auctionRepository.findByBatchId(batchId)
                .orElseThrow(() -> new AuctionNotFoundException(batchId));

        if (!"OPEN".equals(auction.getStatus())) {
            Bid rejected = new Bid(auction.getId(), request.getBuyerId(), request.getAmount(),
                    "REJECTED", Instant.now());
            Bid saved = bidRepository.save(rejected);
            return new BidResponse(saved.getId().toString(), "REJECTED", "Auction is not open");
        }

        // Backend enforces the accept/reject rule; AI only supplied the
        // floor value (Section 2.2 / Section 6 rule #3).
        if (request.getAmount() < auction.getFloorPrice()) {
            Bid rejected = new Bid(auction.getId(), request.getBuyerId(), request.getAmount(),
                    "REJECTED", Instant.now());
            Bid saved = bidRepository.save(rejected);
            return new BidResponse(saved.getId().toString(), "REJECTED",
                    "Bid is below the fair price floor of " + auction.getFloorPrice());
        }

        Bid accepted = new Bid(auction.getId(), request.getBuyerId(), request.getAmount(),
                "ACCEPTED", Instant.now());
        Bid saved = bidRepository.save(accepted);

        auction.closeWithWinner(request.getAmount(), request.getBuyerId());
        auctionRepository.save(auction);

        SaleConfirmedEvent saleEvent = new SaleConfirmedEvent(
                auction.getBatchId(), request.getAmount(), request.getBuyerId()
        );
        kafkaTemplate.send(saleConfirmedTopic, auction.getBatchId(), saleEvent);

        // TODO: price-deviation publish — Section 4.2 lists this topic
        // as Backend-produced when "price deviates from fair band," but
        // doesn't define the deviation threshold/condition. Needs team
        // decision before implementing. Flagged, not silently skipped.

        return new BidResponse(saved.getId().toString(), "ACCEPTED", "Bid accepted, auction closed");
    }

    public AuctionStatusResponse getStatus(String batchId) {
        Auction auction = auctionRepository.findByBatchId(batchId)
                .orElseThrow(() -> new AuctionNotFoundException(batchId));

        return new AuctionStatusResponse(
                auction.getBatchId(),
                auction.getStatus(),
                auction.getFloorPrice(),
                auction.getWinningBid(),
                auction.getWinningBuyerId()
        );
    }
}
