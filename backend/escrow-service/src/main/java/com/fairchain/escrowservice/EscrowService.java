package com.fairchain.escrowservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EscrowService {

    private final TransactionRepository repository;
    private final KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate;
    private final String paymentReleasedTopic;

    public EscrowService(TransactionRepository repository,
                          KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate,
                          @Value("${fairchain.kafka.topic.payment-released}") String paymentReleasedTopic) {
        this.repository = repository;
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
        repository.save(transaction);
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.delivery-event}", groupId = "escrow-service")
    public void onDeliveryEvent(DeliveryEventPayload event) {
        repository.findByBatchId(event.getBatchId()).ifPresent(transaction -> {
            if ("PENDING".equals(transaction.getEscrowStatus())) {
                Instant now = Instant.now();
                transaction.release(now);
                repository.save(transaction);

                PaymentReleasedEvent released = new PaymentReleasedEvent(
                        transaction.getBatchId(),
                        transaction.getBuyerId(),
                        transaction.getAmount(),
                        now
                );
                kafkaTemplate.send(paymentReleasedTopic, transaction.getBatchId(), released);
            }
            // If no transaction found or already released, silently no-op —
            // could add logging/error handling here if the team wants stricter enforcement.
        });
    }
}
