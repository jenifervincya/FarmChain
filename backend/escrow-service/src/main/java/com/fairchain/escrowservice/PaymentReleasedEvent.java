package com.fairchain.escrowservice;

import java.time.Instant;

/**
 * Outgoing payload for payment-released topic. Not explicitly listed
 * in Section 4.2's topic table (only mentioned in Section 1.4 step 7's
 * workflow narrative) — flagged: this topic needs to be added to
 * Section 4.2's table officially.
 */
public class PaymentReleasedEvent {
    private String batchId;
    private String buyerId;
    private Double amount;
    private Instant releasedAt;

    public PaymentReleasedEvent() {}

    public PaymentReleasedEvent(String batchId, String buyerId, Double amount, Instant releasedAt) {
        this.batchId = batchId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.releasedAt = releasedAt;
    }

    public String getBatchId() { return batchId; }
    public String getBuyerId() { return buyerId; }
    public Double getAmount() { return amount; }
    public Instant getReleasedAt() { return releasedAt; }
}
