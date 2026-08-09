package com.fairchain.batchservice;

import java.time.Instant;

public class BatchDetailResponse {

    private final String batchId;
    private final String trackingCode;
    private final String farmerId;
    private final String crop;
    private final Double quantityKg;
    private final String region;
    private final String status;
    private final Instant createdAt;

    public BatchDetailResponse(String batchId, String trackingCode, String farmerId,
                                String crop, Double quantityKg, String region,
                                String status, Instant createdAt) {
        this.batchId = batchId;
        this.trackingCode = trackingCode;
        this.farmerId = farmerId;
        this.crop = crop;
        this.quantityKg = quantityKg;
        this.region = region;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getBatchId() { return batchId; }
    public String getTrackingCode() { return trackingCode; }
    public String getFarmerId() { return farmerId; }
    public String getCrop() { return crop; }
    public Double getQuantityKg() { return quantityKg; }
    public String getRegion() { return region; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
