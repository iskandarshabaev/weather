package com.ishabaev.weather.data;

import com.ishabaev.weather.dao.OrmWeather;

import java.util.List;

/**
 * Created by ishabaev on 21.06.16.
 */
public class Day {

    private List<OrmWeather> hours;

    public List<OrmWeather> getHours() {
        return hours;
    }

    public void setHours(List<OrmWeather> hours) {
        this.hours = hours;
    }
}
