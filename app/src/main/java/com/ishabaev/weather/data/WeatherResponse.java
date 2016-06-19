package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ishabaev on 17.06.16.
 */
public class WeatherResponse {

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("main")
    private Main main;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }
}
