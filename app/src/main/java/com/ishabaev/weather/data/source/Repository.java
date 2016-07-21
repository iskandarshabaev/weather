package com.ishabaev.weather.data.source;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by ishabaev on 24.06.16.
 */
public class Repository implements DataSource {

    private static Repository INSTANCE;
    private DataSource mLocalDataSource;
    private DataSource mRemoteDataSource;

    private Repository(DataSource localDataSource,
                       DataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static Repository getInstance(DataSource localDataSource,
                                         DataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, Date date,
                                                    boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return mLocalDataSource.getForecast(cityId, date, true)
                    .flatMap(Observable::from)
                    .toSortedList((ormWeather1, ormWeather2) -> {
                        return ormWeather1.getDt().compareTo(ormWeather2.getDt());
                    })
                    .flatMap(ormWeathers -> {
                        if (ormWeathers.size() == 0) {
                            return getForecastFromRemoteDataSource(cityId, true);
                        }
                        Calendar currenTime = Calendar.getInstance();
                        currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                        if (ormWeathers.get(0).getDt().before(currenTime.getTime())) {
                            return getForecastFromRemoteDataSource(cityId, true);
                        } else {
                            return Observable.just(ormWeathers);
                        }
                    });
        } else {
            return mLocalDataSource.getForecast(cityId, date, false);
        }
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return mLocalDataSource.getForecast(cityId, true)
                    .flatMap(Observable::from)
                    .toSortedList((ormWeather1, ormWeather2) -> {
                        return ormWeather1.getDt().compareTo(ormWeather2.getDt());
                    })
                    .flatMap(ormWeathers -> {
                        if (ormWeathers.size() == 0) {
                            return getForecastFromRemoteDataSource(cityId, true);
                        }
                        Calendar currenTime = Calendar.getInstance();
                        currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                        if (ormWeathers.get(0).getDt().before(currenTime.getTime())) {
                            return getForecastFromRemoteDataSource(cityId, true);
                        } else {
                            return Observable.just(ormWeathers);
                        }
                    });
        } else {
            return mLocalDataSource.getForecast(cityId, false);
        }
    }

    private Observable<List<OrmWeather>> getForecastFromRemoteDataSource(final int cityId,
                                                                         boolean isNetworkAvailable) {
        return mRemoteDataSource
                .getForecast(cityId, isNetworkAvailable)
                .onErrorResumeNext(mLocalDataSource.getForecast(cityId, false))
                .doOnNext(ormWeathers -> mLocalDataSource.refreshForecast(cityId, ormWeathers));
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
