package com.ishabaev.weather.data.source.local;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.Date;
import java.util.List;

import rx.Observable;

public interface ILocalDataSource {

    Observable<List<OrmWeather>> getForecast(int cityId);

    Observable<List<OrmWeather>> getForecast(int cityId, Date date);

    Observable<OrmWeather> getSingleForecast(final int cityId);

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
