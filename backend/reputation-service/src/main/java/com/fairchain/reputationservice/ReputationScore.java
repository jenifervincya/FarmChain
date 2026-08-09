package com.fairchain.reputationservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reputation_scores")
public class ReputationScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id", nullable = false, unique = true)
    private String entityId;

    @Column(name = "entity_type", nullable = false)
    private String entityType; // e.g. "middleman", "buyer"

    @Column(nullable = false)
    private Double score;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    protected ReputationScore() {
    }

    public ReputationScore(String entityId, String entityType, Double score, Instant lastUpdated) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.score = score;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() { return id; }
    public String getEntityId() { return entityId; }
    public String getEntityType() { return entityType; }
    public Double getScore() { return score; }
    public Instant getLastUpdated() { return lastUpdated; }

    public void updateScore(Double newScore, Instant updatedAt) {
        this.score = newScore;
        this.lastUpdated = updatedAt;
    }
}
