package com.fairchain.reputationservice;

import java.time.Instant;

public class ReputationResponse {

    private final String entityId;
    private final String entityType;
    private final Double score;
    private final Instant lastUpdated;

    public ReputationResponse(String entityId, String entityType, Double score, Instant lastUpdated) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.score = score;
        this.lastUpdated = lastUpdated;
    }

    public String getEntityId() { return entityId; }
    public String getEntityType() { return entityType; }
    public Double getScore() { return score; }
    public Instant getLastUpdated() { return lastUpdated; }
}
