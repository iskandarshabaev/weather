package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 21.06.16.
 */
public class Rain {

    @SerializedName("3h")
    private double val;

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
