package com.ishabaev.weather.data.source;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.local.ILocalDataSource;
import com.ishabaev.weather.data.source.remote.IRemoteDataSource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by ishabaev on 24.06.16.
 */
public class Repository implements RepositoryDataSource {

    private static Repository INSTANCE;
    private ILocalDataSource mLocalDataSource;
    private IRemoteDataSource mRemoteDataSource;

    private Repository(ILocalDataSource localDataSource,
                       IRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static Repository getInstance(ILocalDataSource localDataSource,
                                         IRemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, Date date,
                                                    boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return mLocalDataSource.getForecast(cityId, date)
                    .flatMap(Observable::from)
                    .toSortedList((ormWeather1, ormWeather2) -> {
                        return ormWeather1.getDt().compareTo(ormWeather2.getDt());
                    })
                    .flatMap(ormWeathers -> {
                        if (ormWeathers.size() == 0) {
                            return getForecastFromRemoteDataSource(cityId);
                        }
                        Calendar currenTime = Calendar.getInstance();
                        currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                        if (ormWeathers.get(0).getDt().before(currenTime.getTime())) {
                            return getForecastFromRemoteDataSource(cityId);
                        } else {
                            return Observable.just(ormWeathers);
                        }
                    });
        } else {
            return mLocalDataSource.getForecast(cityId, date);
        }
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return mLocalDataSource.getForecast(cityId)
                    .flatMap(Observable::from)
                    .toSortedList((ormWeather1, ormWeather2) -> {
                        return ormWeather1.getDt().compareTo(ormWeather2.getDt());
                    })
                    .flatMap(ormWeathers -> {
                        if (ormWeathers.size() == 0) {
                            return getForecastFromRemoteDataSource(cityId);
                        }
                        Calendar currenTime = Calendar.getInstance();
                        currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                        if (ormWeathers.get(0).getDt().before(currenTime.getTime())) {
                            return getForecastFromRemoteDataSource(cityId);
                        } else {
                            return Observable.just(ormWeathers);
                        }
                    });
        } else {
            return mLocalDataSource.getForecast(cityId);
        }
    }

    public Observable<OrmWeather> getSingleForecast(final int cityId, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return mLocalDataSource.getSingleForecast(cityId)
                    .flatMap(ormWeather -> {
                        Calendar currenTime = Calendar.getInstance();
                        currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                        if (ormWeather == null) {
                            return getSingleForecastFromRemoteDataSource(cityId);
                        }
                        if (ormWeather.getDt().before(currenTime.getTime())) {
                            return getSingleForecastFromRemoteDataSource(cityId);
                        } else {
                            return Observable.just(ormWeather);
                        }
                    });
        } else {
            return mLocalDataSource.getSingleForecast(cityId);
        }
    }

    private Observable<List<OrmWeather>> getForecastFromRemoteDataSource(final int cityId) {
        return mRemoteDataSource
                .getForecast(cityId)
                .onErrorResumeNext(mLocalDataSource.getForecast(cityId))
                .doOnNext(ormWeathers -> mLocalDataSource.refreshForecast(cityId, ormWeathers));
    }

    private Observable<OrmWeather> getSingleForecastFromRemoteDataSource(final int cityId) {
        return mRemoteDataSource
                .getForecast(cityId)
                .onErrorResumeNext(mLocalDataSource.getForecast(cityId))
                .doOnNext(ormWeathers -> mLocalDataSource.refreshForecast(cityId, ormWeathers))
                .flatMap(Observable::from)
                .first();
    }

    @Override
    public Observable<List<OrmCity>> getCityList() {
        return mLocalDataSource.getCityList();
    }

    @Override
    public void deleteCity(OrmCity city) {
        mLocalDataSource.deleteCity(city);
    }

    @Override
    public void saveCities(List<OrmCity> cities) {
        mLocalDataSource.saveCities(cities);
    }

    @Override
    public void saveCity(OrmCity city) {
        mLocalDataSource.saveCity(city);
    }

    @Override
    public void refreshAllForecast(List<OrmWeather> forecast) {
        mLocalDataSource.refreshAllForecast(forecast);
    }

    @Override
    public void refreshForecast(int cityId, List<OrmWeather> forecast) {
        mLocalDataSource.refreshForecast(cityId, forecast);
    }

    @Override
    public void deleteAllForecast() {
        mLocalDataSource.deleteAllForecast();
    }

    @Override
    public void deleteForecast(int cityId) {
        mLocalDataSource.deleteForecast(cityId);
    }

    @Override
    public void saveForecast(List<OrmWeather> forecast) {
        mLocalDataSource.saveForecast(forecast);
    }
}
