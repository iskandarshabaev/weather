package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.dao.OrmWeather;

import java.util.Date;
import java.util.List;

/**
 * Created by ishabaev on 25.06.16.
 */
public interface DayWeatherContract {

    interface View {

        void addWeathersToList(List<OrmWeather> weatherList);

        void addWeatherToList(OrmWeather weather);

        boolean isNetworkAvailable();
    }

    interface UserActionsListener {

        void loadDayForecast(int cityId, Date date);
    }
}
