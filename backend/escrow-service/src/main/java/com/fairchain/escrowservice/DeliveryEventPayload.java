package com.fairchain.escrowservice;

import java.time.Instant;

/** Incoming payload from delivery-event topic (Section 4.2). */
public class DeliveryEventPayload {
    private String batchId;
    private Instant timestamp;
    private String confirmedBy;

    public DeliveryEventPayload() {}

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(String confirmedBy) { this.confirmedBy = confirmedBy; }
}
