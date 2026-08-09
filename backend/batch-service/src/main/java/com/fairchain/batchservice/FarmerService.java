package com.fairchain.batchservice;

import org.springframework.stereotype.Service;

@Service
public class FarmerService {

    private final FarmerRepository repository;

    public FarmerService(FarmerRepository repository) {
        this.repository = repository;
    }

    public FarmerResponse registerFarmer(FarmerRequest request) {
        Farmer farmer = new Farmer(
                request.getName(),
                request.getPhone(),
                request.getRegion(),
                request.getPreferredLanguage()
        );
        Farmer saved = repository.save(farmer);
        return toResponse(saved);
    }

    public FarmerResponse getFarmer(String farmerId) {
        Farmer farmer = repository.findById(Long.valueOf(farmerId))
                .orElseThrow(() -> new FarmerNotFoundException(farmerId));
        return toResponse(farmer);
    }

    private FarmerResponse toResponse(Farmer farmer) {
        return new FarmerResponse(
                farmer.getId().toString(),
                farmer.getName(),
                farmer.getPhone(),
                farmer.getRegion(),
                farmer.getPreferredLanguage()
        );
    }
}
