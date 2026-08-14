package com.fairchain.escrowservice;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String batchId) {
        super("No transaction found for batch: " + batchId);
    }
}
