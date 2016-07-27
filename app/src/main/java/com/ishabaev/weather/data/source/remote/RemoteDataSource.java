package com.ishabaev.weather.data.source.remote;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.model.WeatherHour;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by ishabaev on 24.06.16.
 */
public class RemoteDataSource implements IRemoteDataSource {

    private static RemoteDataSource INSTANCE;
    private OpenWeatherService mService;

    private RemoteDataSource(OpenWeatherService service) {
        mService = service;
    }

    public static RemoteDataSource getInstance(OpenWeatherService service) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(service);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(int cityId) {
        return mService.getForecast2(ApiClient.APPID, cityId, ApiClient.UNITS)
                .flatMap(sub -> {
                    List<OrmWeather> forecast = new ArrayList<>(sub.getList().size());
                    for (WeatherHour hour : sub.getList()) {
                        OrmWeather weather = new OrmWeather();
                        weather.setCity_id(sub.getCity().getId());
                        weather.setCity_name(sub.getCity().getName());
                        weather.setDt(new Date(hour.getDt() * 1000));
                        weather.setClouds(hour.getClouds().getAll());
                        weather.setHumidity(hour.getMain().getHumidity());
                        weather.setPressure(hour.getMain().getPressure());
                        weather.setTemp(hour.getMain().getTemp());
                        weather.setTemp_min(hour.getMain().getTempMin());
                        weather.setTemp_max(hour.getMain().getTempMax());
                        weather.setIcon(hour.getWeather().get(0).getIcon());
                        if (hour.getWind() != null) {
                            weather.setWind_speed(hour.getWind().getSpeed());
                        }
                        weather.setRain(hour.getRain() == null ? 0.0 : hour.getRain().getVal());
                        weather.setSnow(hour.getSnow() == null ? 0.0 : hour.getSnow().getVal());
                        forecast.add(weather);
                    }
                    return Observable.just(forecast);
                });
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(int cityId, Date date) {
        return Observable.empty();
    }
}
