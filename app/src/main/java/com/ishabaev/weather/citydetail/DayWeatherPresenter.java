package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.DataSource;
import com.ishabaev.weather.data.source.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ishabaev on 25.06.16.
 */
public class DayWeatherPresenter implements DayWeatherContract.UserActionsListener {

    private Repository mRepository;
    private DayWeatherContract.View mView;

    public DayWeatherPresenter(DayWeatherContract.View view, Repository repository) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void loadDayForecast(int cityId, Date date) {
        mRepository.getForecast(cityId, mView.isNetworkAvailable(),
                date, new DataSource.LoadWeatherCallback() {
                    @Override
                    public void onWeatherLoaded(List<OrmWeather> forecast) {
                        mView.addWeathersToList(forecast);
                    }

                    @Override
                    public void onDataNotAvailable(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
