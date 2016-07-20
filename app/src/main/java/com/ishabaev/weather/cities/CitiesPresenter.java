package com.ishabaev.weather.cities;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;
import com.ishabaev.weather.data.source.Repository;

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
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
        mBackgroundScheduler = background;
        mMainScheduler = main;
    }

    @Override
    public void subscribe() {

    }

    private Observable<CityWithWeather> getCityWithWeather(final OrmCity city) {
        return mRepository.getForecast(city.get_id().intValue(), mView.isNetworkAvailable())
                .flatMap(ormWeathers -> {
                    CityWithWeather cityWithWeather = new CityWithWeather();
                    cityWithWeather.setCity(city);
                    if (ormWeathers.size() > 0) {
                        cityWithWeather.setWeather(ormWeathers.get(0));//TODO must be fixed
                    } else {
                        OrmWeather emptyWeather = new OrmWeather();
                        cityWithWeather.setWeather(emptyWeather);
                    }
                    return Observable.just(cityWithWeather);
                });
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadCities() {
        mView.setRefreshing(true);
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getCityList()
                .flatMap(city -> Observable.from(city))
                .toSortedList((city1, city2) -> {
                    return city1.getCity_name().compareTo(city2.getCity_name());
                })
                .flatMap(tasks -> Observable.from(tasks))
                .flatMap(city -> getCityWithWeather(city))
                .toList()
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        cities -> mView.setCities(cities),
                        e -> e.printStackTrace(),
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
