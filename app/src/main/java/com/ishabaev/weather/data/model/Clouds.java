package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private double mAll;

    public double getAll() {
        return mAll;
    }

    public void setAll(double all) {
        this.mAll = all;
    }
}
