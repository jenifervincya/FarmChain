package com.fairchain.auctionservice;

import jakarta.persistence.*;

@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false, unique = true)
    private String batchId;

    @Column(name = "farmer_id", nullable = false)
    private String farmerId;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String status; // OPEN, CLOSED

    @Column(name = "floor_price", nullable = false)
    private Double floorPrice;

    @Column(name = "winning_bid")
    private Double winningBid;

    @Column(name = "winning_buyer_id")
    private String winningBuyerId;

    protected Auction() {
    }

    public Auction(String batchId, String farmerId, String crop, String region,
                    String status, Double floorPrice) {
        this.batchId = batchId;
        this.farmerId = farmerId;
        this.crop = crop;
        this.region = region;
        this.status = status;
        this.floorPrice = floorPrice;
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public String getFarmerId() { return farmerId; }
    public String getCrop() { return crop; }
    public String getRegion() { return region; }
    public String getStatus() { return status; }
    public Double getFloorPrice() { return floorPrice; }
    public Double getWinningBid() { return winningBid; }
    public String getWinningBuyerId() { return winningBuyerId; }

    public void closeWithWinner(Double winningBid, String winningBuyerId) {
        this.status = "CLOSED";
        this.winningBid = winningBid;
        this.winningBuyerId = winningBuyerId;
    }
}
