package com.ishabaev.weather.citydetail;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.BaseView;
import com.ishabaev.weather.data.Day;

import java.util.List;

/**
 * Created by ishabaev on 18.06.16.
 */
public interface CityDetailContract {

    interface View extends BaseView<Presenter> {

        void showProgressBar(boolean show);

        void setTemp(String temperature);

        void setHummidity(String hummidity);

        void setWindSpeed(String windSpeed);

        void setPressure(String pressure);

        void setDate(String date);

        void setImage(Drawable drawable);

        void setImage(String assetName);

        void addDays(List<Day> days);

        boolean isNetworkAvailable();

        Resources getResources();

        void showNoForecast();

        void showSnackBar(String text);
    }

    interface Presenter extends BasePresenter {

        void openCity(int cityId);
    }
}
