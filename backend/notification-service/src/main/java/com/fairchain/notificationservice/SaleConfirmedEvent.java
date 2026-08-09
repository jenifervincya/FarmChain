package com.fairchain.notificationservice;

public class SaleConfirmedEvent {
    private String batchId;
    private Double price;
    private String buyerId;

    public SaleConfirmedEvent() {}

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
}
