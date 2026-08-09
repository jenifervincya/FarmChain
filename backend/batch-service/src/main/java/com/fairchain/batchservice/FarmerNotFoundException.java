package com.fairchain.batchservice;

public class FarmerNotFoundException extends RuntimeException {
    public FarmerNotFoundException(String farmerId) {
        super("No farmer found with id: " + farmerId);
    }
}
