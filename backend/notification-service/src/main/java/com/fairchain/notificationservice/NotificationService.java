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

    @KafkaListener(topics = "${fairchain.kafka.topic.sale-confirmed}", groupId = "notification-service")
    public void onSaleConfirmed(SaleConfirmedEvent event) {
        String message = String.format(
                "Your batch sold for %.2f. Payment will follow on delivery confirmation.",
                event.getPrice()
        );
        save(event.getFarmerId(), event.getBatchId(), "SALE_CONFIRMED", message, "PENDING");
    }

    /**
     * Now resolves — payment-released carries farmerId as of the
     * escrow-service Transaction/PaymentReleasedEvent extension.
     */
    @KafkaListener(topics = "${fairchain.kafka.topic.payment-released}", groupId = "notification-service")
    public void onPaymentReleased(PaymentReleasedEvent event) {
        String message = String.format(
                "Payment of %.2f has been released for your batch.",
                event.getAmount()
        );
        save(event.getFarmerId(), event.getBatchId(), "PAYMENT_RELEASED", message, "PENDING");
    }

    private void save(String farmerId, String batchId, String eventType, String message, String status) {
        NotificationLog log = new NotificationLog(farmerId, batchId, eventType, message, status, Instant.now());
        repository.save(log);
    }
}
