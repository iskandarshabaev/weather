package com.ishabaev.weather.cities;

import android.content.Context;

import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.dao.CityDao;
import com.ishabaev.weather.dao.DaoMaster;
import com.ishabaev.weather.dao.DaoSession;
import com.ishabaev.weather.dao.Weather;
import com.ishabaev.weather.dao.WeatherDao;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public class CitiesPresenter implements CitiesContract.UserActionsListener {

    private CityDao mCityDao;
    private WeatherDao mWeatherDao;
    private CitiesContract.View mView;

    public CitiesPresenter(CitiesContract.View view) {
        mView = view;
    }

    public void initDao(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        daoSession.clear();
        mCityDao = daoSession.getCityDao();
        mWeatherDao = daoSession.getWeatherDao();
    }

    @Override
    public void loadCities() {
        List<City> cities = mCityDao.loadAll();
        if (cities.size() == 0) {
            cities = initDefaultCities();
        }

        List<Long> cityIds = new ArrayList<>(cities.size());
        for (City city : cities) {
            cityIds.add(city.get_id());
        }

        List<Weather> weatherList = mWeatherDao.queryBuilder()
                .where(WeatherDao.Properties.City_id.in(cityIds))
                .build()
                .list();

        List<CityWithWeather> cityWithWeatherList = new ArrayList<>();

        for (City city : cities) {
            cityWithWeatherList.add(new CityWithWeather(city,findCityWeather(city, weatherList)));
        }

        mView.addCitiesToList(cityWithWeatherList);
    }

    private Weather findCityWeather(City city, List<Weather> weatherList) {
        for (Weather weather : weatherList) {
            if (city.get_id().equals(weather.getCity_id())) {
                return weather;
            }
        }
        return null;
    }

    public List<City> initDefaultCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City((long) 536203, "St Petersburg", "RU", 59.916668, 30.25));
        cities.add(new City((long) 524901, "Moscow", "RU", 55.75222, 37.615555));
        cities.add(new City((long) 551487, "Kazan", "RU", 55.788738, 49.122139));
        mCityDao.insertInTx(cities);
        return cities;
    }

    @Override
    public void saveCities(List<City> cities) {
        mCityDao.insertInTx(cities);
    }

    @Override
    public void saveCity(City city) {
        mCityDao.insert(city);
    }
}
