package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.List;

/**
 * Created by ishabaev on 21.06.16.
 */
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
