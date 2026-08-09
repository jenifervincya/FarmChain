package com.fairchain.ledgerservice;

import java.time.Instant;

/**
 * Shared Kafka payload shape for pickup-event, transport-event,
 * warehouse-event, delivery-event (Section 4.2). confirmedBy is
 * only populated for delivery-event; null otherwise.
 */
public class LedgerEventPayload {

    private String batchId;
    private Instant timestamp;
    private String location;
    private String confirmedBy;

    public LedgerEventPayload() {
    }

    public LedgerEventPayload(String batchId, Instant timestamp, String location, String confirmedBy) {
        this.batchId = batchId;
        this.timestamp = timestamp;
        this.location = location;
        this.confirmedBy = confirmedBy;
    }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(String confirmedBy) { this.confirmedBy = confirmedBy; }
}
