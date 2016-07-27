package com.ishabaev.weather.cities;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.data.source.model.CityWithWeather;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 19.06.16.
 */
public class CitiesPresenter implements CitiesContract.Presenter {

    private CitiesContract.View mView;
    private Repository mRepository;
    private CompositeSubscription mSubscriptions;
    private Scheduler mBackgroundScheduler;
    private Scheduler mMainScheduler;

    public CitiesPresenter(CitiesContract.View view, Repository repository,
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

    /*private Observable<CityWithWeather> getCityWithWeather(final OrmCity city) {
        return mRepository.getForecast(city.get_id().intValue(), mView.isNetworkAvailable())
                .flatMap(ormWeathers -> {
                    CityWithWeather cityWithWeather = new CityWithWeather();
                    cityWithWeather.setCity(city);
                    if (ormWeathers.size() > 0) {
                        cityWithWeather.setWeather(ormWeathers.get(0));
                    } else {
                        OrmWeather emptyWeather = new OrmWeather();
                        cityWithWeather.setWeather(emptyWeather);
                    }
                    return Observable.just(cityWithWeather);
                });
    }*/


    private Observable<CityWithWeather> getCityWithWeather(final OrmCity city) {
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
        mView.setRefreshing(true);
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getCityList()
                .flatMap(Observable::from)
                .toSortedList((city1, city2) -> city1.getCity_name().compareTo(city2.getCity_name()))
                .flatMap(Observable::from)
                .flatMap(this::getCityWithWeather)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        cities -> mView.addCityToList(cities),
                        throwable -> {
                            mView.setRefreshing(false);
                            throwable.printStackTrace();
                        },
                        () -> mView.setRefreshing(false)
                );
        mSubscriptions.add(subscription);
    }

    @Override
    public void saveCities(List<OrmCity> cities) {
        mRepository.saveCities(cities);
    }

    @Override
    public void saveCity(OrmCity city) {
        mRepository.saveCity(city);
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
