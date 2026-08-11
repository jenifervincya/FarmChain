package com.fairchain.auctionservice;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // ACCEPTED, REJECTED

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Bid() {
    }

    public Bid(Long auctionId, String buyerId, Double amount, String status, Instant createdAt) {
        this.auctionId = auctionId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getAuctionId() { return auctionId; }
    public String getBuyerId() { return buyerId; }
    public Double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
