package com.fairchain.notificationservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmer_id")
    private String farmerId;

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status;

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

    public void markSent() {
        this.status = "SENT";
    }
}
