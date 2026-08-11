package com.fairchain.auctionservice;

import java.time.Instant;

public class BatchRegisteredEvent {
    private String batchId;
    private String farmerId;
    private String crop;
    private String region;
    private Double quantity;
    private Instant timestamp;

    public BatchRegisteredEvent() {}

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
