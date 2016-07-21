package com.ishabaev.weather.citydetail;

import com.ishabaev.weather.data.source.Repository;

import java.util.Date;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 25.06.16.
 */
public class DayWeatherPresenter implements DayWeatherContract.UserActionsListener {

    private Repository mRepository;
    private DayWeatherContract.View mView;
    private CompositeSubscription mSubscriptions;

    public DayWeatherPresenter(DayWeatherContract.View view, Repository repository) {
        mRepository = repository;
        mView = view;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadDayForecast(int cityId, Date date) {
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getForecast(cityId, date, mView.isNetworkAvailable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ormWeathers -> mView.addWeathersToList(ormWeathers),
                        Throwable::printStackTrace
                );
        mSubscriptions.add(subscription);
    }
}
