package com.fairchain.escrowservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "expected_price", nullable = false)
    private Double expectedPrice;

    @Column(name = "actual_price", nullable = false)
    private Double actualPrice;

    @Column(name = "payout_amount")
    private Double payoutAmount;

    @Column(name = "triggered_at", nullable = false)
    private Instant triggeredAt;

    protected InsuranceClaim() {
    }

    public InsuranceClaim(String batchId, Double expectedPrice, Double actualPrice,
                           Double payoutAmount, Instant triggeredAt) {
        this.batchId = batchId;
        this.expectedPrice = expectedPrice;
        this.actualPrice = actualPrice;
        this.payoutAmount = payoutAmount;
        this.triggeredAt = triggeredAt;
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public Double getExpectedPrice() { return expectedPrice; }
    public Double getActualPrice() { return actualPrice; }
    public Double getPayoutAmount() { return payoutAmount; }
    public Instant getTriggeredAt() { return triggeredAt; }
}
