package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.List;

public class Day {

    @SerializedName("hours")
    private List<OrmWeather> mHours;

    public List<OrmWeather> getHours() {
        return mHours;
    }

    public void setHours(List<OrmWeather> hours) {
        this.mHours = hours;
    }
}
