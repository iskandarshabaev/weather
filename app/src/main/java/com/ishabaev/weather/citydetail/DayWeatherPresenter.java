package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.data.source.RepositoryDataSource;

import java.util.Date;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 25.06.16.
 */
public class DayWeatherPresenter implements DayWeatherContract.Presenter {

    private RepositoryDataSource mRepository;
    private DayWeatherContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Scheduler mBackgroundScheduler;
    private Scheduler mMainScheduler;

    public DayWeatherPresenter(DayWeatherContract.View view, RepositoryDataSource repository,
                               Scheduler background, Scheduler main) {
        mRepository = repository;
        mView = view;
        mSubscriptions = new CompositeSubscription();
        mBackgroundScheduler = background;
        mMainScheduler = main;
    }

    @Override
    public void loadDayForecast(int cityId, Date date) {
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getForecast(cityId, date, mView.isNetworkAvailable())
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        ormWeathers -> mView.addWeathersToList(ormWeathers),
                        Throwable::printStackTrace
                );
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
