package com.example.client.data;

public class BusinessData {
    private long businessId;
    private String title;
    private String createdAt;

    public BusinessData() {
    }

    public BusinessData(long businessId, String title, String createdAt) {
        this.businessId = businessId;
        this.title = title;
        this.createdAt = createdAt;
    }
    public long getBusinessId() {
        return businessId;
    }
    public String getTitle() {
        return this.title;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }
}