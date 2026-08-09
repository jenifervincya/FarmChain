package com.fairchain.ledgerservice;

import jakarta.validation.constraints.NotBlank;

public class EventRequest {

    @NotBlank
    private String batchId;

    @NotBlank
    private String eventType; // pickup, transport, warehouse, delivery

    @NotBlank
    private String location;

    private String metadataJson; // optional — e.g. temperature for transport events

    private String confirmedBy; // used for delivery-event per Section 4.2

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }

    public String getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(String confirmedBy) { this.confirmedBy = confirmedBy; }
}
