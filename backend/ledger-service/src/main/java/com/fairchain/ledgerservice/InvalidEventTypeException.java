package com.fairchain.ledgerservice;

public class InvalidEventTypeException extends RuntimeException {
    public InvalidEventTypeException(String eventType) {
        super("Unknown event type: " + eventType + ". Must be one of: pickup, transport, warehouse, delivery");
    }
}
