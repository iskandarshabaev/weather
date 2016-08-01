package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ishabaev on 17.06.16.
 */
public class WeatherResponse {

    @SerializedName("coord")
    private Coord mCoord;

    @SerializedName("weather")
    private List<Weather> mWeather;

    @SerializedName("main")
    private Main mMain;

    public void setCoord(Coord coord) {
        this.mCoord = coord;
    }

    public void setWeather(List<Weather> weather) {
        this.mWeather = weather;
    }

    public void setMain(Main main) {
        this.mMain = main;
    }

    public Coord getCoord() {
        return mCoord;
    }

    public List<Weather> getWeather() {
        return mWeather;
    }

    public Main getMain() {
        return mMain;
    }
}
