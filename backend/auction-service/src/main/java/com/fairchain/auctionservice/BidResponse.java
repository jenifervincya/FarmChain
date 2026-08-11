package com.fairchain.auctionservice;

public class BidResponse {
    private final String bidId;
    private final String status;
    private final String message;

    public BidResponse(String bidId, String status, String message) {
        this.bidId = bidId;
        this.status = status;
        this.message = message;
    }

    public String getBidId() { return bidId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
