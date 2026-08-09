package com.fairchain.batchservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BatchRequest {

    @NotBlank
    private String farmerId;

    @NotBlank
    private String crop;

    @NotNull
    @Positive
    private Double quantityKg;

    @NotBlank
    private String region;

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }

    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
