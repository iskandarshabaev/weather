package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class WeatherHour {

    @SerializedName("dt")
    private long mDt;

    @SerializedName("main")
    private Main mMain;

    @SerializedName("clouds")
    private Clouds mClouds;

    @SerializedName("wind")
    private Wind mWind;

    @SerializedName("rain")
    private Rain mRain;

    @SerializedName("snow")
    private Snow mSnow;

    @SerializedName("dt_txt")
    private String mDtTxt;

    @SerializedName("weather")
    private List<Weather> mWeather;

    public long getDt() {
        return mDt;
    }

    public void setDt(long dt) {
        this.mDt = dt;
    }

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        this.mMain = main;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public void setClouds(Clouds clouds) {
        this.mClouds = clouds;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        this.mWind = wind;
    }

    public Rain getRain() {
        return mRain;
    }

    public void setRain(Rain rain) {
        this.mRain = rain;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public void setSnow(Snow snow) {
        this.mSnow = snow;
    }

    public String getDt_txt() {
        return mDtTxt;
    }

    public void setDt_txt(String dt_txt) {
        this.mDtTxt = dt_txt;
    }

    public List<Weather> getWeather() {
        return mWeather;
    }

    public void setWeather(List<Weather> weather) {
        this.mWeather = weather;
    }
}
