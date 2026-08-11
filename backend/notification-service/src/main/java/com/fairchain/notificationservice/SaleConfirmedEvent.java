package com.fairchain.notificationservice;

public class SaleConfirmedEvent {
    private String batchId;
    private Double price;
    private String buyerId;
    private String farmerId;
    private String crop;
    private String region;

    public SaleConfirmedEvent() {}

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }
    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
