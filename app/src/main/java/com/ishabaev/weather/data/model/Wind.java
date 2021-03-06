package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    private double mSpeed;

    @SerializedName("deg")
    private double mDeg;

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public double getDeg() {
        return mDeg;
    }

    public void setDeg(double deg) {
        this.mDeg = deg;
    }
}
