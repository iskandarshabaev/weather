package com.ishabaev.weather.addcity;

import android.text.TextUtils;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.source.FileManager;
import com.ishabaev.weather.data.source.Repository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 28.06.16.
 */
public class AddCityPresenter implements AddCityContract.Presenter {

    private AddCityContract.View mView;
    private Repository mRepository;
    private FileManager mFileManager;
    private CompositeSubscription mSubscriptions;

    public AddCityPresenter(AddCityContract.View view, Repository repository, FileManager fileManager) {
        mView = view;
        mRepository = repository;
        mFileManager = fileManager;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onItemClick(OrmCity city) {
        mRepository.saveCity(city);
    }

    @Override
    public void textChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            mView.clearCities();
            mView.setImageViewVisible(true);
            mView.setSearchStateVisibile(true);
            mView.showStartTyping();
            return;
        }
        mSubscriptions.clear();
        mView.clearCities();
        mView.setProgressBarVisibile(true);
        Subscription subscription = mFileManager.searchCity(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        city -> {
                            mView.setImageViewVisible(false);
                            mView.setSearchStateVisibile(false);
                            mView.addCityToList(city);
                        },
                        throwable -> {
                            mView.showCouldNotFindCity();
                            mView.setSearchStateVisibile(true);
                            mView.setImageViewVisible(true);
                            mView.setProgressBarVisibile(false);
                            throwable.printStackTrace();
                        },
                        () -> mView.setProgressBarVisibile(false)
                );
        mSubscriptions.add(subscription);
    }
}
