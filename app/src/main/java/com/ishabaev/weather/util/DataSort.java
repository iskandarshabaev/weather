package com.ishabaev.weather.util;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ishabaev on 25.06.16.
 */
public class DataSort {
    public static void sortWeatherHour(List<OrmWeather> forecast) {
        Collections.sort(forecast, new Comparator<OrmWeather>() {
            @Override
            public int compare(OrmWeather o1, OrmWeather o2) {
                return o1.getDt().compareTo(o2.getDt());
            }
        });
    }

    public static void sortCityWithWeatherList(List<CityWithWeather> cityWithWeatherList) {
        Collections.sort(cityWithWeatherList, new Comparator<CityWithWeather>() {
            @Override
            public int compare(CityWithWeather o1, CityWithWeather o2) {
                return o1.getCity().getCity_name().compareTo(o2.getCity().getCity_name());
            }
        });
    }
}
