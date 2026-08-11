package com.fairchain.auctionservice;

public class SaleConfirmedEvent {
    private String batchId;
    private Double price;
    private String buyerId;

    public SaleConfirmedEvent() {}

    public SaleConfirmedEvent(String batchId, Double price, String buyerId) {
        this.batchId = batchId;
        this.price = price;
        this.buyerId = buyerId;
    }

    public String getBatchId() { return batchId; }
    public Double getPrice() { return price; }
    public String getBuyerId() { return buyerId; }
}
