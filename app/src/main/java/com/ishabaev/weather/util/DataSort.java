package com.ishabaev.weather.util;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.Collections;
import java.util.List;

/**
 * Created by ishabaev on 25.06.16.
 */
public class DataSort {
    public static void sortWeatherHour(List<OrmWeather> forecast) {
        Collections.sort(forecast, (o1, o2) -> o1.getDt().compareTo(o2.getDt()));
    }

    public static void sortCityWithWeatherList(List<CityWithWeather> cityWithWeatherList) {
        Collections.sort(cityWithWeatherList, (o1, o2) ->
                o1.getCity().getCity_name().compareTo(o2.getCity().getCity_name()));
    }
}
