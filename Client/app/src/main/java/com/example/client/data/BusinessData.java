package com.example.client.data;

public class BusinessData {
    private String title;
    private String createdAt;

    public BusinessData() {
    }

    public BusinessData(String title, String createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }
}