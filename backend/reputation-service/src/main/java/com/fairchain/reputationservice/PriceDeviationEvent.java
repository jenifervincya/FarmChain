package com.fairchain.reputationservice;

public class PriceDeviationEvent {
    private String batchId;
    private double expectedMinPrice;
    private double actualPrice;
    private String[] nodeChain;

    public PriceDeviationEvent() {}

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public double getExpectedMinPrice() { return expectedMinPrice; }
    public void setExpectedMinPrice(double expectedMinPrice) { this.expectedMinPrice = expectedMinPrice; }
    public double getActualPrice() { return actualPrice; }
    public void setActualPrice(double actualPrice) { this.actualPrice = actualPrice; }
    public String[] getNodeChain() { return nodeChain; }
    public void setNodeChain(String[] nodeChain) { this.nodeChain = nodeChain; }
}
