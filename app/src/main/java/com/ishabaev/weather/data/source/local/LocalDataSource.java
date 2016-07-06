package com.ishabaev.weather.data.source.local;

import android.content.Context;

import com.ishabaev.weather.dao.DaoMaster;
import com.ishabaev.weather.dao.DaoSession;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmCityDao;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.dao.OrmWeatherDao;
import com.ishabaev.weather.data.source.DataSource;
import com.ishabaev.weather.util.DataSort;

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
    public Observable<List<OrmWeather>> getForecast(final int cityId, boolean isNetworkAvailable) {
        return Observable.create(
                new Observable.OnSubscribe<List<OrmWeather>>() {
                    @Override
                    public void call(Subscriber<? super List<OrmWeather>> sub) {
                        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
                        List<OrmWeather> forecast = weatherDao.queryBuilder()
                                .where(OrmWeatherDao.Properties.City_id.eq(cityId))
                                .build()
                                .list();
                        sub.onNext(forecast);
                        sub.onCompleted();
                    }
                }
        );
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(final int cityId, final Date date, boolean isNetworkAvailable) {
        return Observable.create(
                new Observable.OnSubscribe<List<OrmWeather>>() {
                    @Override
                    public void call(Subscriber<? super List<OrmWeather>> sub) {
                        OrmWeatherDao weatherDao = mDaoSession.getOrmWeatherDao();
                        List<OrmWeather> forecast = weatherDao.queryBuilder()
                                .where(OrmWeatherDao.Properties.Dt.between(getStartOfDayInMillis(date),
                                        getEndOfDayInMillis(date)),
                                        OrmWeatherDao.Properties.City_id.eq(cityId))
                                .build().list();
                        DataSort.sortWeatherHour(forecast);
                        sub.onNext(forecast);
                        sub.onCompleted();
                    }
                }
        );
    }

    @Override
    public Observable<List<OrmCity>> getCityList() {
        return Observable.create(
                new Observable.OnSubscribe<List<OrmCity>>() {
                    @Override
                    public void call(Subscriber<? super List<OrmCity>> sub) {
                        OrmCityDao cityDao = mDaoSession.getOrmCityDao();
                        List<OrmCity> cities = cityDao.loadAll();
                        if (cities.size() > 0) {
                            sub.onNext(cities);
                        } else {
                            cities.add(new OrmCity((long) 498817, "Saint Petersburg", "RU", 59.894444, 30.264168));
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
    }

    private Date getStartOfDayInMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDayInMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfDayInMillis(date));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
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
        weatherDao.queryBuilder()
                .where(OrmWeatherDao.Properties.City_id.eq(cityId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
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
