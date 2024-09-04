package com.example.client.data;

public class TurbinesData {
    private String title;
    private String engTitle;
    private String imgUrl;

    public TurbinesData(String title, String engTitle) {
        this.title = title;
        this.engTitle = engTitle;
    }

    public String getTitle() { return this.title; }
    public String getEngTitle() { return this.engTitle; }
}
