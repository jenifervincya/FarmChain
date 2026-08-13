package com.fairchain.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationService {

    private final NotificationLogRepository repository;
    private final ObjectMapper objectMapper;

    public NotificationService(NotificationLogRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.batch-registered}", groupId = "notification-service")
    public void onBatchRegistered(String rawPayload) throws Exception {
        BatchRegisteredEvent event = objectMapper.readValue(rawPayload, BatchRegisteredEvent.class);
        String message = String.format(
                "Your batch of %.0fkg %s has been registered. Tracking updates will follow.",
                event.getQuantity(), event.getCrop()
        );
        save(event.getFarmerId(), event.getBatchId(), "BATCH_REGISTERED", message, "PENDING");
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.sale-confirmed}", groupId = "notification-service")
    public void onSaleConfirmed(String rawPayload) throws Exception {
        SaleConfirmedEvent event = objectMapper.readValue(rawPayload, SaleConfirmedEvent.class);
        String message = String.format(
                "Your batch sold for %.2f. Payment will follow on delivery confirmation.",
                event.getPrice()
        );
        save(event.getFarmerId(), event.getBatchId(), "SALE_CONFIRMED", message, "PENDING");
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.payment-released}", groupId = "notification-service")
    public void onPaymentReleased(String rawPayload) throws Exception {
        PaymentReleasedEvent event = objectMapper.readValue(rawPayload, PaymentReleasedEvent.class);
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
