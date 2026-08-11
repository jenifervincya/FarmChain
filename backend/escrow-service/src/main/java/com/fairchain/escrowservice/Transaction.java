package com.fairchain.escrowservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false, unique = true)
    private String batchId;

    @Column(name = "farmer_id", nullable = false)
    private String farmerId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "escrow_status", nullable = false)
    private String escrowStatus; // PENDING, RELEASED

    @Column(name = "released_at")
    private Instant releasedAt;

    protected Transaction() {
    }

    public Transaction(String batchId, String farmerId, String buyerId, Double amount,
                        String escrowStatus, Instant releasedAt) {
        this.batchId = batchId;
        this.farmerId = farmerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.escrowStatus = escrowStatus;
        this.releasedAt = releasedAt;
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public String getFarmerId() { return farmerId; }
    public String getBuyerId() { return buyerId; }
    public Double getAmount() { return amount; }
    public String getEscrowStatus() { return escrowStatus; }
    public Instant getReleasedAt() { return releasedAt; }

    public void release(Instant releasedAt) {
        this.escrowStatus = "RELEASED";
        this.releasedAt = releasedAt;
    }
}
