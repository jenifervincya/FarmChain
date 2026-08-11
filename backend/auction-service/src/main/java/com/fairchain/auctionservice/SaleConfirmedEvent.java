package com.fairchain.auctionservice;

/**
 * Extended beyond Section 4.2's original payload (batchId, price, buyerId)
 * to add farmerId, crop, region — needed by escrow-service's insurance
 * check and notification-service's farmer alerts. Backend owns Kafka
 * topic schema per Section 2.1, so this is within scope, but flagged
 * per Section 6 rule #2 since it changes a shared contract.
 */
public class SaleConfirmedEvent {
    private String batchId;
    private Double price;
    private String buyerId;
    private String farmerId;
    private String crop;
    private String region;

    public SaleConfirmedEvent() {}

    public SaleConfirmedEvent(String batchId, Double price, String buyerId,
                               String farmerId, String crop, String region) {
        this.batchId = batchId;
        this.price = price;
        this.buyerId = buyerId;
        this.farmerId = farmerId;
        this.crop = crop;
        this.region = region;
    }

    public String getBatchId() { return batchId; }
    public Double getPrice() { return price; }
    public String getBuyerId() { return buyerId; }
    public String getFarmerId() { return farmerId; }
    public String getCrop() { return crop; }
    public String getRegion() { return region; }
}
