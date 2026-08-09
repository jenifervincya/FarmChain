package com.fairchain.escrowservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EscrowService {

    private final TransactionRepository transactionRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate;
    private final String paymentReleasedTopic;

    public EscrowService(TransactionRepository transactionRepository,
                          InsuranceClaimRepository insuranceClaimRepository,
                          KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate,
                          @Value("${fairchain.kafka.topic.payment-released}") String paymentReleasedTopic) {
        this.transactionRepository = transactionRepository;
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentReleasedTopic = paymentReleasedTopic;
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.sale-confirmed}", groupId = "escrow-service")
    public void onSaleConfirmed(SaleConfirmedEvent event) {
        // Open a pending escrow transaction now; released later on delivery-event.
        Transaction transaction = new Transaction(
                event.getBatchId(),
                event.getBuyerId(),
                event.getPrice(),
                "PENDING",
                null
        );
        transactionRepository.save(transaction);

        checkInsurancePool(event);
    }

    /**
     * Insurance Pool check per Section 1.3/4.2 ("triggers Insurance Pool
     * check if price is below band"). INCOMPLETE — two real blockers:
     *
     * 1. No fair-price band available: needs AI's /ai/fair-price-band,
     *    which isn't live yet (same blocker as auction-service).
     * 2. sale-confirmed's payload (Section 4.2) only has batchId, price,
     *    buyerId — no crop/region, which fair-price-band needs. Would
     *    require a batch-service lookup first; no such internal
     *    endpoint exists yet.
     *
     * Flagged per Section 6 rule #7 rather than silently faked. Placeholder
     * below does NOT call AI and does NOT create real insurance claims —
     * it just documents where that logic goes.
     */
    private void checkInsurancePool(SaleConfirmedEvent event) {
        // TODO: fetch crop/region for event.getBatchId() from batch-service
        // TODO: call AI's GET /ai/fair-price-band?crop=&region=
        // TODO: if event.getPrice() < band.minPrice, create an InsuranceClaim
        //       and calculate payoutAmount, then persist via insuranceClaimRepository

        // Intentionally not implemented yet — see class-level comment.
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.delivery-event}", groupId = "escrow-service")
    public void onDeliveryEvent(DeliveryEventPayload event) {
        transactionRepository.findByBatchId(event.getBatchId()).ifPresent(transaction -> {
            if ("PENDING".equals(transaction.getEscrowStatus())) {
                Instant now = Instant.now();
                transaction.release(now);
                transactionRepository.save(transaction);

                PaymentReleasedEvent released = new PaymentReleasedEvent(
                        transaction.getBatchId(),
                        transaction.getBuyerId(),
                        transaction.getAmount(),
                        now
                );
                kafkaTemplate.send(paymentReleasedTopic, transaction.getBatchId(), released);
            }
        });
    }
}
