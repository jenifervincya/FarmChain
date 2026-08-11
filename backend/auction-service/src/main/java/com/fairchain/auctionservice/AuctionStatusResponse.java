package com.fairchain.auctionservice;

public class AuctionStatusResponse {
    private final String batchId;
    private final String status;
    private final Double floorPrice;
    private final Double winningBid;
    private final String winningBuyerId;

    public AuctionStatusResponse(String batchId, String status, Double floorPrice,
                                  Double winningBid, String winningBuyerId) {
        this.batchId = batchId;
        this.status = status;
        this.floorPrice = floorPrice;
        this.winningBid = winningBid;
        this.winningBuyerId = winningBuyerId;
    }

    public String getBatchId() { return batchId; }
    public String getStatus() { return status; }
    public Double getFloorPrice() { return floorPrice; }
    public Double getWinningBid() { return winningBid; }
    public String getWinningBuyerId() { return winningBuyerId; }
}
