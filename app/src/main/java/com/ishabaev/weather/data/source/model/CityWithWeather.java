package com.ishabaev.weather.data.source.model;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;

/**
 * Created by ishabaev on 22.06.16.
 */
public class CityWithWeather {

    private OrmCity mCity;
    private OrmWeather mWeather;

    public CityWithWeather() {
    }

    public CityWithWeather(OrmCity city, OrmWeather weather) {
        mCity = city;
        mWeather = weather;
    }

    public OrmCity getCity() {
        return mCity;
    }

    public void setCity(OrmCity city) {
        this.mCity = city;
    }

    public OrmWeather getWeather() {
        return mWeather;
    }

    public void setWeather(OrmWeather weather) {
        this.mWeather = weather;
    }
}
