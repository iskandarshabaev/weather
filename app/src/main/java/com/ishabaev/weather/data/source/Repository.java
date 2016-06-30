package com.ishabaev.weather.data.source;

import android.content.res.AssetManager;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by ishabaev on 24.06.16.
 */
public class Repository implements DataSource {

    private static Repository INSTANCE;
    private DataSource mLocalDataSource;
    private DataSource mRemoteDataSource;
    //private boolean mDataOutDated = true;
    private AssetManager mAssetManager;

    private Repository(AssetManager assetManager,
                       DataSource localDataSource,
                       DataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mAssetManager = assetManager;
    }

    public static Repository getInstance(AssetManager assetManager,
                                         DataSource localDataSource,
                                         DataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(assetManager, localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getForecast(final int cityId, final boolean isNetworkAvailable,
                            final LoadWeatherCallback callback) {
        //if (mDataOutDated) {
        //getForecastFromRemoteDataSource(cityId, callback);
        //} else {
        mLocalDataSource.getForecast(cityId, isNetworkAvailable, new LoadWeatherCallback() {
            @Override
            public void onWeatherLoaded(List<OrmWeather> forecast) {
                callback.onWeatherLoaded(forecast);
            }

            @Override
            public void onDataNotAvailable(Throwable t) {
                getForecastFromRemoteDataSource(cityId, isNetworkAvailable, callback);
            }
        });
        //}
    }

    @Override
    public void getForecast(int cityId, final boolean isNetworkAvailable, Date date, LoadWeatherCallback callback) {
        mLocalDataSource.getForecast(cityId, isNetworkAvailable, date, callback);
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, final boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            Observable<List<OrmWeather>> ford = mLocalDataSource.getForecast(cityId, isNetworkAvailable)
                    .flatMap(new Func1<List<OrmWeather>, Observable<OrmWeather>>() {
                        @Override
                        public Observable<OrmWeather> call(List<OrmWeather> ormWeathers) {
                            return Observable.from(ormWeathers);
                        }
                    })
                    .toSortedList(new Func2<OrmWeather, OrmWeather, Integer>() {
                        @Override
                        public Integer call(OrmWeather ormWeather1, OrmWeather ormWeather2) {
                            return ormWeather1.getDt().compareTo(ormWeather2.getDt());
                        }
                    })
                    .flatMap(new Func1<List<OrmWeather>, Observable<List<OrmWeather>>>() {
                        @Override
                        public Observable<List<OrmWeather>> call(List<OrmWeather> ormWeathers) {
                            if(ormWeathers.size() == 0){
                                return getForecastFromRemoteDataSource(cityId, isNetworkAvailable);
                            }
                            Calendar currenTime = Calendar.getInstance();
                            currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                            if (ormWeathers.get(0).getDt().before(currenTime.getTime())) {
                                return getForecastFromRemoteDataSource(cityId, isNetworkAvailable);
                            }else {
                                return Observable.just(ormWeathers);
                            }
                        }
                    });
            return ford;
        } else {
            return mLocalDataSource.getForecast(cityId, isNetworkAvailable);
        }
    }

    private Observable<List<OrmWeather>> getForecastFromRemoteDataSource(final int cityId, final boolean isNetworkAvailable) {
        Observable<List<OrmWeather>> observable = mRemoteDataSource
                .getForecast(cityId,isNetworkAvailable)
                .doOnNext(new Action1<List<OrmWeather>>() {
                    @Override
                    public void call(List<OrmWeather> ormWeathers) {
                        mLocalDataSource.saveForecast(ormWeathers);
                    }
                });

        return observable;
    }

    private void getForecastFromRemoteDataSource(final int cityId, final boolean isNetworkAvailable,
                                                 final LoadWeatherCallback callback) {
        mRemoteDataSource.getForecast(cityId, isNetworkAvailable, new LoadWeatherCallback() {
            @Override
            public void onWeatherLoaded(List<OrmWeather> forecast) {
                //mDataOutDated = false;
                refreshForecast(cityId, forecast);
                callback.onWeatherLoaded(forecast);
            }

            @Override
            public void onDataNotAvailable(Throwable t) {
                callback.onDataNotAvailable(t);
            }
        });
    }

    @Override
    public void getCityWithWeather(final OrmCity city, final boolean isNetworkAvailable,
                                   final LoadCityWithWeatherCallback callback) {
        getForecast(city.get_id().intValue(), isNetworkAvailable, new LoadWeatherCallback() {
            @Override
            public void onWeatherLoaded(List<OrmWeather> forecast) {
                CityWithWeather cityWithWeather = new CityWithWeather();
                cityWithWeather.setCity(city);
                cityWithWeather.setWeather(findCityWeather(city, forecast));
                callback.onCityLoaded(cityWithWeather);
            }

            @Override
            public void onDataNotAvailable(Throwable t) {
                callback.onDataNotAvailable(t);
            }
        });
    }


    /*
    public List<CityWithWeather> getCitiesWithWeather(List<OrmCity> cities) {
        List<CityWithWeather> cityWithWeatherList = new ArrayList<>();
        for (OrmCity city : cities) {
            CityWithWeather cityWithWeather = new CityWithWeather();
            cityWithWeather.setCity(city);
            cityWithWeather.setWeather(findCityWeather(city, getForecast(city.get_id().intValue())));
            cityWithWeatherList.add(cityWithWeather);
        }
        return cityWithWeatherList;
    }

    @Override
    public List<OrmWeather> getForecast(int cityId) {

        return null;
    }
    */

    private OrmWeather findCityWeather(OrmCity city, List<OrmWeather> weatherList) {
        for (OrmWeather weather : weatherList) {
            if (city.get_id().equals(weather.getCity_id())) {
                return weather;
            }
        }
        return null;
    }

    @Override
    public void getCityList(final LoadCitiesCallback callback) {
        mLocalDataSource.getCityList(new LoadCitiesCallback() {
            @Override
            public void onCitiesLoaded(List<OrmCity> cities) {
                callback.onCitiesLoaded(cities);
            }

            @Override
            public void onDataNotAvailable(Throwable t) {
                List<OrmCity> cities = new ArrayList<>();
                cities.add(new OrmCity((long) 536203, "St Petersburg", "RU", 59.916668, 30.25));
                cities.add(new OrmCity((long) 524901, "Moscow", "RU", 55.75222, 37.615555));
                cities.add(new OrmCity((long) 551487, "Kazan", "RU", 55.788738, 49.122139));
                cities.add(new OrmCity((long) 2759794, "Amsterdam", "NL", 52.374031, 4.88969));
                cities.add(new OrmCity((long) 5809844, "Seattle", "US", 47.606209, -122.332069));
                cities.add(new OrmCity((long) 2147714, "Sydney", "AU", -33.867851, 151.207321));
                saveCities(cities);
                callback.onCitiesLoaded(cities);
            }
        });
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

    public InputStream open(String fileName) throws IOException {
        return mAssetManager.open(fileName);
    }
}
