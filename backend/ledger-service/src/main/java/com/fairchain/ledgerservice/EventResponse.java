package com.fairchain.ledgerservice;

import java.time.Instant;

public class EventResponse {

    private final String eventId;
    private final String batchId;
    private final String eventType;
    private final Instant timestamp;
    private final String hash;

    public EventResponse(String eventId, String batchId, String eventType,
                          Instant timestamp, String hash) {
        this.eventId = eventId;
        this.batchId = batchId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.hash = hash;
    }

    public String getEventId() { return eventId; }
    public String getBatchId() { return batchId; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public String getHash() { return hash; }
}
