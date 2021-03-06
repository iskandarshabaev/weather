package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    private Double mLon;

    @SerializedName("lat")
    private Double mLat;

    public Double getLon() {
        return mLon;
    }

    public void setLon(Double lon) {
        this.mLon = lon;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        this.mLat = lat;
    }
}
