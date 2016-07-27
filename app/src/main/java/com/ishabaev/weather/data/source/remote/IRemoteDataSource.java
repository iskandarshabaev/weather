package com.ishabaev.weather.data.source.remote;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by ishabaev on 24.06.16.
 */
public interface IRemoteDataSource {

    Observable<List<OrmWeather>> getForecast(int cityId);

    Observable<List<OrmWeather>> getForecast(int cityId, Date date);
}
