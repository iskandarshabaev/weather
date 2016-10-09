package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.model.Day;
import com.ishabaev.weather.data.source.RepositoryDataSource;
import com.ishabaev.weather.util.DataSort;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CityDetailPresenter implements CityDetailContract.Presenter {

    private CityDetailContract.View mView;
    private RepositoryDataSource mRepository;
    private CompositeSubscription mSubscriptions;
    private Scheduler mBackgroundScheduler;
    private Scheduler mMainScheduler;

    public CityDetailPresenter(CityDetailContract.View cityDetailView, RepositoryDataSource repository,
                               Scheduler background, Scheduler main) {
        mView = cityDetailView;
        mRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mBackgroundScheduler = background;
        mMainScheduler = main;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void openCity(int cityId) {
        //mView.showProgressBar(true);
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getForecast(cityId, mView.isNetworkAvailable())
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        this::makeView,
                        Throwable::printStackTrace,
                        () -> mView.showProgressBar(false)
                );
        mSubscriptions.add(subscription);
    }

    private void makeView(List<OrmWeather> forecast) {
        DataSort.sortWeatherHour(forecast);
        if (forecast.size() > 0) {
            OrmWeather current = forecast.get(0);
            mView.setTemp((int)current.getTemp());
            mView.setHumidity(current.getHumidity());
            mView.setWindSpeed(current.getWind_speed());
            mView.setPressure(current.getPressure());
            setDate(current.getDt());
            //mView.setImage(current.getIcon() + ".jpg");
            addDaysToViewPager(forecast);
        } else {
            mView.showError();
        }
    }

    private void setDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM, HH:mm", Locale.getDefault());
        String value = format.format(date);
        mView.setDate(value);
    }

    private void addDaysToViewPager(List<OrmWeather> hours) {
        List<Day> days = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(hours.get(0).getDt());
        Day day = new Day();
        day.setHours(new ArrayList<>());
        days.add(day);
        for (OrmWeather hourWeather : hours) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(hourWeather.getDt());
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == 0) {
                day.getHours().add(hourWeather);
            } else {
                day = new Day();
                day.setHours(new ArrayList<>());
                day.getHours().add(hourWeather);
                days.add(day);
            }
            c1 = c2;
        }
        mView.addDays(days);
    }
}
