package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
