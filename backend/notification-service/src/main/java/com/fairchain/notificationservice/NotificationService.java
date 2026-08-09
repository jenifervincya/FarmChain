package com.fairchain.notificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationService {

    private final NotificationLogRepository repository;

    public NotificationService(NotificationLogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.batch-registered}", groupId = "notification-service")
    public void onBatchRegistered(BatchRegisteredEvent event) {
        String message = String.format(
                "Your batch of %.0fkg %s has been registered. Tracking updates will follow.",
                event.getQuantity(), event.getCrop()
        );
        save(event.getFarmerId(), event.getBatchId(), "BATCH_REGISTERED", message, "PENDING");
    }

    /**
     * INCOMPLETE: sale-confirmed's payload (Section 4.2) has no farmerId,
     * only buyerId. Logged with status UNRESOLVED — needs a batchId ->
     * farmerId lookup (e.g. an internal endpoint on batch-service) before
     * this can actually be sent. Flagged rather than guessing/faking it.
     */
    @KafkaListener(topics = "${fairchain.kafka.topic.sale-confirmed}", groupId = "notification-service")
    public void onSaleConfirmed(SaleConfirmedEvent event) {
        String message = String.format(
                "Your batch sold for %.2f. Payment will follow on delivery confirmation.",
                event.getPrice()
        );
        save(null, event.getBatchId(), "SALE_CONFIRMED", message, "UNRESOLVED");
    }

    /**
     * INCOMPLETE: same gap as onSaleConfirmed — payment-released has no
     * farmerId in its payload.
     */
    @KafkaListener(topics = "${fairchain.kafka.topic.payment-released}", groupId = "notification-service")
    public void onPaymentReleased(PaymentReleasedEvent event) {
        String message = String.format(
                "Payment of %.2f has been released for your batch.",
                event.getAmount()
        );
        save(null, event.getBatchId(), "PAYMENT_RELEASED", message, "UNRESOLVED");
    }

    private void save(String farmerId, String batchId, String eventType, String message, String status) {
        NotificationLog log = new NotificationLog(farmerId, batchId, eventType, message, status, Instant.now());
        repository.save(log);
    }
}
