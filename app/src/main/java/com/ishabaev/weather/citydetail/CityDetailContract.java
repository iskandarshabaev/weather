package com.ishabaev.weather.citydetail;

import android.content.Context;

import com.ishabaev.weather.data.Day;

import java.util.List;

/**
 * Created by ishabaev on 18.06.16.
 */
public interface CityDetailContract {

    interface View{

        void showProgressBar(boolean show);

        void setTemp(String temperature);

        void setHummidity(String hummidity);

        void setWindSpeed(String windSpeed);

        void setDate(String date);

        void addDays(List<Day> days);
    }

    interface UserActionsListener{

        void openCity(int cityId);

        void initDao(Context context);
    }
}
