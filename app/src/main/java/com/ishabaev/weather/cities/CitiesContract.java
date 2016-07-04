package com.ishabaev.weather.cities;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.BaseView;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public interface CitiesContract {

    interface View extends BaseView<Presenter>{

        void addCitiesToList(List<CityWithWeather> cities);

        void setCities(List<CityWithWeather> cities);

        void addCityToList(CityWithWeather cityWithWeather);

        void setRefreshing(boolean refreshing);

        boolean isNetworkAvailable();

        boolean isActive();
    }

    interface Presenter extends BasePresenter{
        void loadCities();

        void saveCities(List<OrmCity> cities);

        void saveCity(OrmCity city);

        void removeCity(OrmCity city);

        void removeWeaher(int cityId);
    }
}
