package com.fairchain.auctionservice;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException(String batchId) {
        super("No auction found for batch: " + batchId);
    }
}
