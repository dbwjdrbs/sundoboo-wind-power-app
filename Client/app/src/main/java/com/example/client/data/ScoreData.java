package com.example.client.data;

public class ScoreData {
    private String title;
    private String observerName;
    private String createdAt;
    private int score1;
    private int score2;
    private int score3;
    private int score4;

    public ScoreData(String title, String observerName, String createdAt, int score1, int score2, int score3, int score4) {
        this.title = title;
        this.observerName = observerName;
        this.createdAt = createdAt;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public int getScore3() {
        return score3;
    }

    public int getScore4() {
        return score4;
    }


    public String getTitle() {
        return title;
    }

    public String getObserverName() {
        return observerName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
