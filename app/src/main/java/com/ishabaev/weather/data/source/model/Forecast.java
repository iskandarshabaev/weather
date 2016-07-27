package com.ishabaev.weather.data.source.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class Forecast {

    @SerializedName("city")
    private City mCity;

    @SerializedName("list")
    private List<WeatherHour> mList;

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        this.mCity = city;
    }

    public List<WeatherHour> getList() {
        return mList;
    }

    public void setList(List<WeatherHour> list) {
        this.mList = list;
    }
}
