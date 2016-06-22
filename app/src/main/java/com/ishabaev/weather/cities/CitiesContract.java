package com.ishabaev.weather.cities;

import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public interface CitiesContract {

    interface View{
        void addCitiesToList(List<CityWithWeather> cities);
    }

    interface UserActionsListener{
        void loadCities();

        void saveCities(List<City> cities);

        void saveCity(City city);
    }
}
