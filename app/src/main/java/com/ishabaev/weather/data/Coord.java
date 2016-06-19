package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 17.06.16.
 */
public class Coord {

    @SerializedName("lon")
    private Double lon;

    @SerializedName("lat")
    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
