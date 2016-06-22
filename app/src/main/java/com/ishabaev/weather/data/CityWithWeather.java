package com.ishabaev.weather.data;

import com.ishabaev.weather.dao.Weather;
import com.ishabaev.weather.dao.City;

/**
 * Created by ishabaev on 22.06.16.
 */
public class CityWithWeather {

    private City mCity;
    private Weather mWeather;

    public CityWithWeather(){

    }

    public CityWithWeather(City city, Weather weather){
        mCity = city;
        mWeather = weather;
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        this.mCity = city;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        this.mWeather = weather;
    }
}
