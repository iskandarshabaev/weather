package com.ishabaev.weather.data.source;

import com.ishabaev.weather.dao.OrmCity;

import rx.Observable;

public interface FileSource {

    Observable<OrmCity> searchCity(String cityName);
}
