package com.fairchain.batchservice;

import java.time.Instant;

/**
 * Kafka payload for the "batch-registered" topic (Section 4.2).
 * Field names must match exactly across all services per the
 * spec's Section 4.2 rule: "Topic names, casing, and payload
 * field names must match exactly across all services."
 */
public class BatchRegisteredEvent {

    private String batchId;
    private String farmerId;
    private String crop;
    private String region;
    private Double quantity;
    private Instant timestamp;

    public BatchRegisteredEvent() {
        // needed for JSON (de)serialization
    }

    public BatchRegisteredEvent(String batchId, String farmerId, String crop,
                                 String region, Double quantity, Instant timestamp) {
        this.batchId = batchId;
        this.farmerId = farmerId;
        this.crop = crop;
        this.region = region;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
