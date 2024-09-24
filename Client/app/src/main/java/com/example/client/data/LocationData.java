package com.example.client.data;

public class LocationData {
    private long locationId;
    private long businessId;
    private long turbineId;
    private double latitude;
    private double longitude;
    private String city;
    private String island;
    private String createdAt;

    // 생성자
    public LocationData(long locationId, long businessId, long turbineId,
                        double latitude, double longitude, String city,
                        String island, String createdAt) {
        this.locationId = locationId;
        this.businessId = businessId;
        this.turbineId = turbineId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.island = island;
        this.createdAt = createdAt;
    }

    // Getters
    public long getLocationId() {
        return locationId;
    }

    public long getBusinessId() {
        return businessId;
    }

    public long getTurbineId() {
        return turbineId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getIsland() {
        return island;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
