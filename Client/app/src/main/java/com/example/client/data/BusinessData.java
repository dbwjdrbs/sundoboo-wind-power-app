package com.example.client.data;

public class BusinessData {
    private long businessId;
    private String name;
    private String createdAt;

    public BusinessData(String name, String createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getName() {
        return this.name;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }
}