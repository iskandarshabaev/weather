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

    interface LoadWeatherCallback{
        void onWeatherLoaded(List<OrmWeather> forecast);
        void onDataNotAvailable(Throwable t);
    }

    interface LoadCitiesCallback{
        void onCitiesLoaded(List<OrmCity> cities);
        void onDataNotAvailable(Throwable t);
    }

    interface LoadCityWithWeatherCallback{
        void onCityLoaded(CityWithWeather cityWithWeather);
        void onDataNotAvailable(Throwable t);
    }

    void getForecast(int cityId, boolean isNetworkAvailable, LoadWeatherCallback callback);

    Observable<List<OrmWeather>> getForecast(int cityId, boolean isNetworkAvailable);

    //List<OrmWeather> getForecast(int cityId);

    void getForecast(int cityId, boolean isNetworkAvailable, Date date, LoadWeatherCallback callback);

    void refreshAllForecast(List<OrmWeather> forecast);

    void refreshForecast(int cityId,List<OrmWeather> forecast);

    void deleteAllForecast();

    void deleteForecast(int cityId);

    void deleteCity(OrmCity city);

    void saveForecast(List<OrmWeather> forecast);

    void getCityList(LoadCitiesCallback callback);

    Observable<List<OrmCity>> getCityList();

    void saveCities(List<OrmCity> cities);

    void saveCity(OrmCity city);

    void getCityWithWeather(OrmCity city, boolean isNetworkAvailable, LoadCityWithWeatherCallback callback);
}
