package com.fairchain.auctionservice;

import java.time.Instant;
import java.util.Map;

public class FairPriceBandResponse {
    private String crop;
    private String region;
    private Double minPrice;
    private Double maxPrice;
    private String currency;
    private Instant computedAt;
    private Map<String, Object> factors;

    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Instant getComputedAt() { return computedAt; }
    public void setComputedAt(Instant computedAt) { this.computedAt = computedAt; }
    public Map<String, Object> getFactors() { return factors; }
    public void setFactors(Map<String, Object> factors) { this.factors = factors; }
}
