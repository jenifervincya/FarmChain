package com.fairchain.batchservice;

import java.time.Instant;

public class PriceHistoryResponse {

    private final String crop;
    private final String region;
    private final Double price;
    private final String source;
    private final Instant recordedAt;

    public PriceHistoryResponse(String crop, String region, Double price, String source, Instant recordedAt) {
        this.crop = crop;
        this.region = region;
        this.price = price;
        this.source = source;
        this.recordedAt = recordedAt;
    }

    public String getCrop() { return crop; }
    public String getRegion() { return region; }
    public Double getPrice() { return price; }
    public String getSource() { return source; }
    public Instant getRecordedAt() { return recordedAt; }
}
