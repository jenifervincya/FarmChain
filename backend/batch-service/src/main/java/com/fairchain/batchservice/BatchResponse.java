package com.fairchain.batchservice;

import java.time.Instant;

public class BatchResponse {

    private final String batchId;
    private final String trackingCode;
    private final String status;
    private final Instant createdAt;

    public BatchResponse(String batchId, String trackingCode, String status, Instant createdAt) {
        this.batchId = batchId;
        this.trackingCode = trackingCode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getBatchId() { return batchId; }
    public String getTrackingCode() { return trackingCode; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
