package com.ishabaev.weather.data.source.local;

import android.content.Context;
import android.os.AsyncTask;

import com.ishabaev.weather.dao.DaoMaster;
import com.ishabaev.weather.dao.DaoSession;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmCityDao;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.dao.OrmWeatherDao;
import com.ishabaev.weather.data.source.DataSource;
import com.ishabaev.weather.util.DataSort;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ishabaev on 24.06.16.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE;
    private DaoSession mDaoSession;

    private LocalDataSource(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getForecast(int cityId,  boolean isNetworkAvailable, LoadWeatherCallback callback) {
        /*
        WeatherDao weatherDao = mDaoSession.getWeatherDao();
        List<Weather> forecast = weatherDao.queryBuilder()
                .where(WeatherDao.Properties.City_id.eq(cityId))
                .build()
                .list();

        if (forecast.size() != 0) {
            DataSort.sortWeatherHour(forecast);
            Calendar currenTime = Calendar.getInstance();
            currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
            if (forecast.get(0).getDt().before(currenTime.getTime())) {
                callback.onDataNotAvailable(new Exception());
                return;
            }
            callback.onWeatherLoaded(forecast);
        } else {
            callback.onDataNotAvailable(new Exception("Blah Blah Blah"));
        }
        */
        LoadWeatherAsync loadWeatherAsync = new LoadWeatherAsync(callback, isNetworkAvailable);
        loadWeatherAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cityId);
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, boolean isNetworkAvailable) {
        Observable<List<OrmWeather>> observable = Observable.create(
                new Observable.OnSubscribe<List<OrmWeather>>() {
                    @Override
                    public void call(Subscriber<? super List<OrmWeather>> sub) {
                        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
                        List<OrmWeather> forecast = weatherDao.queryBuilder()
                                .where(OrmWeatherDao.Properties.City_id.eq(cityId))
                                .build()
                                .list();
                        if (forecast.size() > 0) {
                            sub.onNext(forecast);
                        }
                        sub.onCompleted();
                    }
                }
        );
        return observable;
    }

    @Override
    public Observable<List<OrmCity>> getCityList() {
        Observable<List<OrmCity>> observable = Observable.create(
                new Observable.OnSubscribe<List<OrmCity>>() {
                    @Override
                    public void call(Subscriber<? super List<OrmCity>> sub) {
                        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
                        List<OrmCity> cities = cityDao.loadAll();
                        if (cities.size() > 0) {
                            sub.onNext(cities);
                        }else {
                            cities.add(new OrmCity((long) 536203, "St Petersburg", "RU", 59.916668, 30.25));
                            cities.add(new OrmCity((long) 524901, "Moscow", "RU", 55.75222, 37.615555));
                            cities.add(new OrmCity((long) 551487, "Kazan", "RU", 55.788738, 49.122139));
                            cities.add(new OrmCity((long) 2759794, "Amsterdam", "NL", 52.374031, 4.88969));
                            cities.add(new OrmCity((long) 5809844, "Seattle", "US", 47.606209, -122.332069));
                            cities.add(new OrmCity((long) 2147714, "Sydney", "AU", -33.867851, 151.207321));
                            saveCities(cities);
                            sub.onNext(cities);
                        }
                        sub.onCompleted();
                    }
                }
        );
        return observable;
    }

    private class LoadWeatherAsync extends AsyncTask<Integer, Void, List<OrmWeather>>{

        private LoadWeatherCallback mCallback;
        private boolean mNetworkAvailable;

        LoadWeatherAsync(LoadWeatherCallback callback, boolean isNetworkAvailable){
            mCallback = callback;
            mNetworkAvailable = isNetworkAvailable;
        }

        @Override
        protected List<OrmWeather> doInBackground(Integer... params) {
            int cityId = params[0];
            OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
            List<OrmWeather> forecast = weatherDao.queryBuilder()
                    .where(OrmWeatherDao.Properties.City_id.eq(cityId))
                    .build()
                    .list();

            if (forecast.size() != 0) {
                DataSort.sortWeatherHour(forecast);
                Calendar currenTime = Calendar.getInstance();
                currenTime.set(Calendar.HOUR_OF_DAY, currenTime.get(Calendar.HOUR_OF_DAY) - 6);
                if (mNetworkAvailable && forecast.get(0).getDt().before(currenTime.getTime())) {
                    //mCallback.onDataNotAvailable(new Exception());
                    this.cancel(false);
                    return null;
                }
                //mCallback.onWeatherLoaded(forecast);
                return forecast;
            } else {
                //mCallback.onDataNotAvailable(new Exception("Blah Blah Blah"));
                this.cancel(false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<OrmWeather> forecast) {
            super.onPostExecute(forecast);
            mCallback.onWeatherLoaded(forecast);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mCallback.onDataNotAvailable(new Exception());
        }
    }

    @Override
    public void getForecast(int cityId,  boolean isNetworkAvailable,
                            Date date, LoadWeatherCallback callback) {
        LoadWeatherAsync2 loadWeatherAsync2 = new LoadWeatherAsync2(callback, date);
        loadWeatherAsync2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cityId);
    }

    private class LoadWeatherAsync2 extends AsyncTask<Integer, Void, List<OrmWeather>>{

        private LoadWeatherCallback mCallback;
        private Date mDate;

        LoadWeatherAsync2(LoadWeatherCallback callback, Date date){
            mCallback = callback;
            mDate = date;
        }

        @Override
        protected List<OrmWeather> doInBackground(Integer... params) {
            int cityId = params[0];
            Calendar c1 = Calendar.getInstance();
            c1.setTime(mDate);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(mDate);
            c1.set(Calendar.HOUR_OF_DAY, 0);

            OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
            List<OrmWeather> weatherList = weatherDao.queryBuilder()
                    .where(OrmWeatherDao.Properties.Dt.between(getStartOfDayInMillis(mDate),
                            getEndOfDayInMillis(mDate)),
                            OrmWeatherDao.Properties.City_id.eq(cityId))
                    .build().list();


            DataSort.sortWeatherHour(weatherList);

            Calendar dayTime = Calendar.getInstance();
            dayTime.setTime(mDate);
            dayTime.set(Calendar.HOUR_OF_DAY, dayTime.get(Calendar.HOUR_OF_DAY) - 6);

            if (weatherList.get(0).getDt().before(dayTime.getTime())) {
                //callback.onDataNotAvailable(new Exception());
                this.cancel(false);
                return null;
            }


            //callback.onWeatherLoaded(weatherList);


            return weatherList;
        }

        @Override
        protected void onPostExecute(List<OrmWeather> forecast) {
            super.onPostExecute(forecast);
            mCallback.onWeatherLoaded(forecast);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mCallback.onDataNotAvailable(new Exception());
        }
    }


    public Date getStartOfDayInMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getEndOfDayInMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfDayInMillis(date));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    @Override
    public void getCityList(LoadCitiesCallback callback) {
        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
        List<OrmCity> cities = cityDao.loadAll();
        if (cities.size() > 0) {
            callback.onCitiesLoaded(cities);
        } else {
            callback.onDataNotAvailable(new Exception());
        }
    }

    @Override
    public void getCityWithWeather(OrmCity city,  boolean isNetworkAvailable,
                                   LoadCityWithWeatherCallback callback) {
        //no need;
    }

    @Override
    public void saveCities(List<OrmCity> cities) {
        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
        cityDao.insertInTx(cities);
    }

    @Override
    public void saveCity(OrmCity city) {
        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
        cityDao.insertOrReplace(city);
    }

    @Override
    public void refreshAllForecast(List<OrmWeather> forecast) {
        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
        weatherDao.deleteAll();
        weatherDao.insertInTx(forecast);
    }

    @Override
    public void refreshForecast(int cityId, List<OrmWeather> forecast) {
        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
        weatherDao.queryBuilder()
                .where(OrmWeatherDao.Properties.City_id.eq((long) cityId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        weatherDao.insertInTx(forecast);
    }

    @Override
    public void deleteAllForecast() {
        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
        weatherDao.deleteAll();
    }

    @Override
    public void deleteForecast(int cityId) {
        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
        weatherDao.deleteByKey((long) cityId);
    }

    @Override
    public void saveForecast(List<OrmWeather> forecast) {
        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
        weatherDao.insertInTx(forecast);
    }

    @Override
    public void deleteCity(OrmCity city) {
        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
        cityDao.delete(city);
    }
}
