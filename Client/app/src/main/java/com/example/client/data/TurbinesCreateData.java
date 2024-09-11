package com.example.client.data;

public class TurbinesCreateData {
    private double objLatitude;
    private double objLongitude;
    private double myLatitude;
    private double myLongitude;
    private double direction;

    public TurbinesCreateData(double objLatitude, double objLongitude, double myLatitude, double myLongitude, double direction) {
        this.objLatitude = objLatitude;
        this.objLongitude = objLongitude;
        this.myLatitude = myLatitude;
        this.myLongitude = myLongitude;
        this.direction = direction;
    }
}
