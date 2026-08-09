package com.fairchain.batchservice;

import jakarta.validation.constraints.NotBlank;

public class FarmerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String region;

    private String preferredLanguage;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}
