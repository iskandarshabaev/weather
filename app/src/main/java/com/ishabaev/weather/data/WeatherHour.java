package com.ishabaev.weather.data;

import java.util.Date;

/**
 * Created by ishabaev on 20.06.16.
 */
public class WeatherHour {

    private long dt;
    private Main main;
    //private Weather weather;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private String dt_txt;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    /*
    public Weather getWeather() {
        return weather;
    }
    */

    /*
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    */

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
