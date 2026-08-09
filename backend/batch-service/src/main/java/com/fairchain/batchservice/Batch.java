package com.fairchain.batchservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_code", nullable = false, unique = true)
    private String trackingCode;

    @Column(name = "farmer_id", nullable = false)
    private String farmerId;

    @Column(nullable = false)
    private String crop;

    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Batch() {
        // JPA requires a no-arg constructor
    }

    public Batch(String trackingCode, String farmerId, String crop,
                 Double quantityKg, String region, String status, Instant createdAt) {
        this.trackingCode = trackingCode;
        this.farmerId = farmerId;
        this.crop = crop;
        this.quantityKg = quantityKg;
        this.region = region;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTrackingCode() { return trackingCode; }
    public String getFarmerId() { return farmerId; }
    public String getCrop() { return crop; }
    public Double getQuantityKg() { return quantityKg; }
    public String getRegion() { return region; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
