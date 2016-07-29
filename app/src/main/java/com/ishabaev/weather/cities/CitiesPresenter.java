package com.ishabaev.weather.cities;

import com.ishabaev.weather.EspressoIdlingResource;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.RepositoryDataSource;
import com.ishabaev.weather.data.source.model.CityWithWeather;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 19.06.16.
 */
public class CitiesPresenter implements CitiesContract.Presenter {

    private CitiesContract.View mView;
    private RepositoryDataSource mRepository;
    private CompositeSubscription mSubscriptions;
    private Scheduler mBackgroundScheduler;
    private Scheduler mMainScheduler;

    public CitiesPresenter(CitiesContract.View view, RepositoryDataSource repository,
                           Scheduler background, Scheduler main) {
        mView = view;
        mRepository = repository;
        mBackgroundScheduler = background;
        mMainScheduler = main;
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
    }

    public Observable<CityWithWeather> getCityWithWeather(final OrmCity city) {
        return mRepository.getSingleForecast(city.get_id().intValue(), mView.isNetworkAvailable())
                .flatMap(ormWeather -> {
                    CityWithWeather cityWithWeather = new CityWithWeather();
                    cityWithWeather.setCity(city);
                    if (ormWeather != null) {
                        cityWithWeather.setWeather(ormWeather);
                    } else {
                        OrmWeather emptyWeather = new OrmWeather();
                        cityWithWeather.setWeather(emptyWeather);
                    }
                    return Observable.just(cityWithWeather);
                });
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }

    @Override
    public void loadCities() {
        EspressoIdlingResource.increment();
        mView.setRefreshing(true);
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getCityList()
                .flatMap(Observable::from)
                .toSortedList((city1, city2) -> city1.getCity_name().compareTo(city2.getCity_name()))
                .flatMap(Observable::from)
                .flatMap(CitiesPresenter.this::getCityWithWeather)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        city -> {
                            mView.addCityToList(city);
                        },
                        throwable -> {
                            EspressoIdlingResource.decrement();
                            mView.setRefreshing(false);
                            throwable.printStackTrace();
                        },
                        () -> {
                            EspressoIdlingResource.decrement();
                            mView.setRefreshing(false);
                        }
                );
        mSubscriptions.add(subscription);
    }

    @Override
    public void removeCity(OrmCity city) {
        mRepository.deleteCity(city);
    }

    @Override
    public void removeWeaher(int cityId) {
        mRepository.deleteForecast(cityId);
    }
}
