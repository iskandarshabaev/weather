package com.ishabaev.weather.data;

import com.ishabaev.weather.dao.Weather;

import java.util.List;

/**
 * Created by ishabaev on 21.06.16.
 */
public class Day {

    private List<Weather> hours;

    public List<Weather> getHours() {
        return hours;
    }

    public void setHours(List<Weather> hours) {
        this.hours = hours;
    }
}
