package com.fairchain.reputationservice;

public class ReputationNotFoundException extends RuntimeException {
    public ReputationNotFoundException(String entityId) {
        super("No reputation score found for entity: " + entityId);
    }
}
