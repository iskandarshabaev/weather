package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.Date;
import java.util.List;

public interface DayWeatherContract {

    interface View {

        void addWeathersToList(List<OrmWeather> weatherList);

        void addWeatherToList(OrmWeather weather);

        boolean isNetworkAvailable();
    }

    interface Presenter extends BasePresenter {

        void loadDayForecast(int cityId, Date date);
    }
}
