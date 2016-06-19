package com.ishabaev.weather.citydetail;

/**
 * Created by ishabaev on 18.06.16.
 */
public interface CityDetailContract {

    interface View{

        void showProgressBar(boolean show);
    }

    interface UserActionsListener{

        void openCity(String cityName);
    }
}
