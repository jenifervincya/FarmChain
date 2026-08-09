package com.fairchain.batchservice;

public class BatchNotFoundException extends RuntimeException {
    public BatchNotFoundException(String trackingCode) {
        super("No batch found with tracking code: " + trackingCode);
    }
}
