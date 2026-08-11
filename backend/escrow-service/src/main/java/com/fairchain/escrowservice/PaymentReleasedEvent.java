package com.fairchain.escrowservice;

import java.time.Instant;

/**
 * Extended with farmerId beyond the original workflow narrative mention
 * (Section 1.4 step 7) — needed by notification-service. Same pattern
 * as the sale-confirmed extension; flagged per Section 6 rule #2.
 */
public class PaymentReleasedEvent {
    private String batchId;
    private String farmerId;
    private String buyerId;
    private Double amount;
    private Instant releasedAt;

    public PaymentReleasedEvent() {}

    public PaymentReleasedEvent(String batchId, String farmerId, String buyerId,
                                 Double amount, Instant releasedAt) {
        this.batchId = batchId;
        this.farmerId = farmerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.releasedAt = releasedAt;
    }

    public String getBatchId() { return batchId; }
    public String getFarmerId() { return farmerId; }
    public String getBuyerId() { return buyerId; }
    public Double getAmount() { return amount; }
    public Instant getReleasedAt() { return releasedAt; }
}
