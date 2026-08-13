package com.fairchain.auctionservice;

/**
 * Matches Section 4.2's payload summary (batchId, expectedBand,
 * actualPrice, nodeChain). Deviation THRESHOLD/trigger condition was
 * never defined in the spec — flagged. Placeholder definition used:
 * a sale that clears at or very close to the floor price (within 2%)
 * is flagged as a deviation worth investigating, since it signals the
 * fair-price mechanism was barely effective. This is a judgment call,
 * not derived from the spec, and should be confirmed with the team.
 */
public class PriceDeviationEvent {
    private String batchId;
    private double expectedMinPrice;
    private double actualPrice;
    private String[] nodeChain;

    public PriceDeviationEvent() {}

    public PriceDeviationEvent(String batchId, double expectedMinPrice,
                                double actualPrice, String[] nodeChain) {
        this.batchId = batchId;
        this.expectedMinPrice = expectedMinPrice;
        this.actualPrice = actualPrice;
        this.nodeChain = nodeChain;
    }

    public String getBatchId() { return batchId; }
    public double getExpectedMinPrice() { return expectedMinPrice; }
    public double getActualPrice() { return actualPrice; }
    public String[] getNodeChain() { return nodeChain; }
}
