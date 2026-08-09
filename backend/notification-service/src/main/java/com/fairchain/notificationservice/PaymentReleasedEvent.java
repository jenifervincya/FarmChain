package com.fairchain.notificationservice;

import java.time.Instant;

public class PaymentReleasedEvent {
    private String batchId;
    private String buyerId;
    private Double amount;
    private Instant releasedAt;

    public PaymentReleasedEvent() {}

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Instant getReleasedAt() { return releasedAt; }
    public void setReleasedAt(Instant releasedAt) { this.releasedAt = releasedAt; }
}
