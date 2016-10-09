package com.ishabaev.weather.citydetail;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.BaseView;
import com.ishabaev.weather.data.model.Day;

import java.util.List;

public interface CityDetailContract {

    interface View extends BaseView<Presenter> {

        void showProgressBar(boolean show);

        void setTemp(int temperature);

        void setHumidity(double humidity);

        void setWindSpeed(double windSpeed);

        void setPressure(double pressure);

        void setDate(@NonNull String date);

        void setImage(@NonNull Drawable drawable);

        void setImage(@NonNull String assetName);

        void addDays(@NonNull List<Day> days);

        boolean isNetworkAvailable();

        void showNoForecast();

        void showError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void openCity(int cityId);
    }
}
