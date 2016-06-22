package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class Forecast {

    @SerializedName("city")
    private City city;
    private List<WeatherHour> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<WeatherHour> getList() {
        return list;
    }

    public void setList(List<WeatherHour> list) {
        this.list = list;
    }
}
