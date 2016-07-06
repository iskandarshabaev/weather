package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 21.06.16.
 */
public class Snow {

    @SerializedName("3h")
    private double mVal;

    public double getVal() {
        return mVal;
    }

    public void setVal(double val) {
        this.mVal = val;
    }
}
