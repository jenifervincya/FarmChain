package com.fairchain.notificationservice;

import java.time.Instant;

public class NotificationResponse {
    private final String id;
    private final String farmerId;
    private final String batchId;
    private final String eventType;
    private final String message;
    private final String status;
    private final Instant createdAt;

    public NotificationResponse(String id, String farmerId, String batchId, String eventType,
                                 String message, String status, Instant createdAt) {
        this.id = id;
        this.farmerId = farmerId;
        this.batchId = batchId;
        this.eventType = eventType;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getFarmerId() { return farmerId; }
    public String getBatchId() { return batchId; }
    public String getEventType() { return eventType; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
