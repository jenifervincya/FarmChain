package com.fairchain.ledgerservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "events")
public class LedgerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    // Hash-chain fields — required by Section 1.3 ("tamper-evident,
    // hash-chained event") but not listed in Section 4.3's table.
    // Flagged to the team; added here as the minimum needed to
    // actually implement hash-chaining.
    @Column(nullable = false, unique = true)
    private String hash;

    @Column(name = "previous_hash")
    private String previousHash;

    protected LedgerEvent() {
    }

    public LedgerEvent(String batchId, String eventType, String location,
                        Instant timestamp, String metadataJson,
                        String hash, String previousHash) {
        this.batchId = batchId;
        this.eventType = eventType;
        this.location = location;
        this.timestamp = timestamp;
        this.metadataJson = metadataJson;
        this.hash = hash;
        this.previousHash = previousHash;
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public String getEventType() { return eventType; }
    public String getLocation() { return location; }
    public Instant getTimestamp() { return timestamp; }
    public String getMetadataJson() { return metadataJson; }
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
}
