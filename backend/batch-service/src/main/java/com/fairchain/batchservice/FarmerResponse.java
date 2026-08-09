package com.fairchain.batchservice;

public class FarmerResponse {

    private final String farmerId;
    private final String name;
    private final String phone;
    private final String region;
    private final String preferredLanguage;

    public FarmerResponse(String farmerId, String name, String phone, String region, String preferredLanguage) {
        this.farmerId = farmerId;
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.preferredLanguage = preferredLanguage;
    }

    public String getFarmerId() { return farmerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRegion() { return region; }
    public String getPreferredLanguage() { return preferredLanguage; }
}
