package com.ishabaev.weather.cities;

import android.support.annotation.NonNull;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.BaseView;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.model.CityWithWeather;

import java.util.List;

public interface CitiesContract {

    interface View extends BaseView<Presenter> {

        void addCitiesToList(@NonNull List<CityWithWeather> cities);

        void setCities(@NonNull List<CityWithWeather> cities);

        void addCityToList(@NonNull CityWithWeather cityWithWeather);

        void setRefreshing(boolean refreshing);

        boolean isNetworkAvailable();

        boolean isActive();

        void showSnackBar(@NonNull String text);
    }

    interface Presenter extends BasePresenter {

        void loadCities();

        void removeCity(@NonNull OrmCity city);

        void removeWeather(int cityId);
    }
}
