package com.ishabaev.weather.data.source.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 20.06.16.
 */
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
