package com.fairchain.batchservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "price_history")
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String source;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    protected PriceHistory() {
    }

    public PriceHistory(String crop, String region, Double price, String source, Instant recordedAt) {
        this.crop = crop;
        this.region = region;
        this.price = price;
        this.source = source;
        this.recordedAt = recordedAt;
    }

    public Long getId() { return id; }
    public String getCrop() { return crop; }
    public String getRegion() { return region; }
    public Double getPrice() { return price; }
    public String getSource() { return source; }
    public Instant getRecordedAt() { return recordedAt; }
}
