package com.fairchain.escrowservice;

import java.time.Instant;

public class TransactionResponse {
    private final String batchId;
    private final String farmerId;
    private final String buyerId;
    private final Double amount;
    private final String escrowStatus;
    private final Instant releasedAt;

    public TransactionResponse(String batchId, String farmerId, String buyerId,
                                Double amount, String escrowStatus, Instant releasedAt) {
        this.batchId = batchId;
        this.farmerId = farmerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.escrowStatus = escrowStatus;
        this.releasedAt = releasedAt;
    }

    public String getBatchId() { return batchId; }
    public String getFarmerId() { return farmerId; }
    public String getBuyerId() { return buyerId; }
    public Double getAmount() { return amount; }
    public String getEscrowStatus() { return escrowStatus; }
    public Instant getReleasedAt() { return releasedAt; }
}
