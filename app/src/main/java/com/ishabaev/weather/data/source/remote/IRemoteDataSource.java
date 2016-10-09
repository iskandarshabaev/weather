package com.ishabaev.weather.data.source.remote;

import com.ishabaev.weather.dao.OrmWeather;

import java.util.Date;
import java.util.List;

import rx.Observable;

public interface IRemoteDataSource {

    Observable<List<OrmWeather>> getForecast(int cityId);

    Observable<List<OrmWeather>> getForecast(int cityId, Date date);
}
