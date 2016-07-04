package com.ishabaev.weather.data.source;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by ishabaev on 24.06.16.
 */
public interface DataSource {

    Observable<List<OrmWeather>> getForecast(int cityId, boolean isNetworkAvailable);

    Observable<List<OrmWeather>> getForecast(int cityId, Date date, boolean isNetworkAvailable);

    void refreshAllForecast(List<OrmWeather> forecast);

    void refreshForecast(int cityId, List<OrmWeather> forecast);

    void deleteAllForecast();

    void deleteForecast(int cityId);

    void deleteCity(OrmCity city);

    void saveForecast(List<OrmWeather> forecast);

    Observable<List<OrmCity>> getCityList();

    void saveCities(List<OrmCity> cities);

    void saveCity(OrmCity city);
}
