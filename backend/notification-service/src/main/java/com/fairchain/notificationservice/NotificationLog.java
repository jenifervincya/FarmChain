package com.fairchain.notificationservice;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Records that a notification SHOULD be sent, and what it should say.
 * This is the "trigger" per Section 3.1 — actual Twilio send is
 * Frontend/DevOps's job. Archana's integration would read PENDING
 * rows here (or consume a Kafka topic we could add) and mark them SENT.
 */
@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmer_id")
    private String farmerId; // nullable — see incomplete listeners below

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "event_type", nullable = false)
    private String eventType; // BATCH_REGISTERED, SALE_CONFIRMED, PAYMENT_RELEASED

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status; // PENDING, SENT, FAILED, UNRESOLVED (missing farmerId)

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected NotificationLog() {
    }

    public NotificationLog(String farmerId, String batchId, String eventType,
                            String message, String status, Instant createdAt) {
        this.farmerId = farmerId;
        this.batchId = batchId;
        this.eventType = eventType;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFarmerId() { return farmerId; }
    public String getBatchId() { return batchId; }
    public String getEventType() { return eventType; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
