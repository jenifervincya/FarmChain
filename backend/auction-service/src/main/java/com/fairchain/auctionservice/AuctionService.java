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
    private final KafkaTemplate<String, SaleConfirmedEvent> saleKafkaTemplate;
    private final KafkaTemplate<String, PriceDeviationEvent> deviationKafkaTemplate;
    private final String saleConfirmedTopic;
    private final String priceDeviationTopic;

    private static final double DEVIATION_THRESHOLD_PCT = 0.02; // placeholder, see PriceDeviationEvent

    public AuctionService(AuctionRepository auctionRepository,
                           BidRepository bidRepository,
                           AiClient aiClient,
                           KafkaTemplate<String, SaleConfirmedEvent> saleKafkaTemplate,
                           KafkaTemplate<String, PriceDeviationEvent> deviationKafkaTemplate,
                           @Value("${fairchain.kafka.topic.sale-confirmed}") String saleConfirmedTopic,
                           @Value("${fairchain.kafka.topic.price-deviation}") String priceDeviationTopic) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.aiClient = aiClient;
        this.saleKafkaTemplate = saleKafkaTemplate;
        this.deviationKafkaTemplate = deviationKafkaTemplate;
        this.saleConfirmedTopic = saleConfirmedTopic;
        this.priceDeviationTopic = priceDeviationTopic;
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.batch-registered}", groupId = "auction-service")
    public void onBatchRegistered(BatchRegisteredEvent event) {
        FairPriceBandResponse band = aiClient.getFairPriceBand(event.getCrop(), event.getRegion());

        Auction auction = new Auction(
                event.getBatchId(),
                event.getFarmerId(),
                event.getCrop(),
                event.getRegion(),
                "OPEN",
                band.getMinPrice()
        );
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
                auction.getBatchId(),
                request.getAmount(),
                request.getBuyerId(),
                auction.getFarmerId(),
                auction.getCrop(),
                auction.getRegion()
        );
        saleKafkaTemplate.send(saleConfirmedTopic, auction.getBatchId(), saleEvent);

        checkPriceDeviation(auction, request.getAmount(), request.getBuyerId());

        return new BidResponse(saved.getId().toString(), "ACCEPTED", "Bid accepted, auction closed");
    }

    // Placeholder deviation check. Threshold definition and rationale are
    // documented in PriceDeviationEvent's class comment. Not derived from
    // the spec - flagged for team review.
    private void checkPriceDeviation(Auction auction, double actualPrice, String buyerId) {
        double threshold = auction.getFloorPrice() * (1 + DEVIATION_THRESHOLD_PCT);
        if (actualPrice <= threshold) {
            PriceDeviationEvent event = new PriceDeviationEvent(
                    auction.getBatchId(),
                    auction.getFloorPrice(),
                    actualPrice,
                    new String[]{"farmer", "auction", buyerId}
            );
            deviationKafkaTemplate.send(priceDeviationTopic, auction.getBatchId(), event);
        }
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
