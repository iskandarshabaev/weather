package com.ishabaev.weather.data.source;

import com.ishabaev.weather.dao.OrmCity;

import rx.Observable;

/**
 * Created by ishabaev on 21.07.16.
 */
public interface FileSource {

    Observable<OrmCity> searchCity(String cityName);
}
