package com.fairchain.batchservice;

import java.time.Instant;

/**
 * Partial implementation — Section 4.1 says this should return the
 * "full price breakdown for customer view," but batch-service only
 * knows about the batch itself. Once auction-service exists, this
 * response should be enriched with winning bid, floor price, and
 * markup breakdown. Flagged: this is not yet the "full" journey.
 */
public class PriceJourneyResponse {

    private final String batchId;
    private final String trackingCode;
    private final String crop;
    private final String region;
    private final String status;
    private final Instant createdAt;

    public PriceJourneyResponse(String batchId, String trackingCode, String crop,
                                 String region, String status, Instant createdAt) {
        this.batchId = batchId;
        this.trackingCode = trackingCode;
        this.crop = crop;
        this.region = region;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getBatchId() { return batchId; }
    public String getTrackingCode() { return trackingCode; }
    public String getCrop() { return crop; }
    public String getRegion() { return region; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
