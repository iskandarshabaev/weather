package com.ishabaev.weather.repositorytest;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

/**
 * Created by ishabaev on 03.07.16.
 */
public class RepositoryTest {

    private static List<OrmCity> CITIES;
    private static List<OrmWeather> FORECAST;

    @Mock
    private Repository mRepository;

    @Before
    public void setupData() {
        MockitoAnnotations.initMocks(this);
        initCities();
        initForecasts();
    }

    private void initCities() {
        CITIES = new ArrayList<>();
        CITIES.add(new OrmCity(54353L, "City1", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54354L, "City2", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54355L, "City3", "RU", 55.4, 34.4));
    }

    private void initForecasts() {
        FORECAST = new ArrayList<>();
        FORECAST.add(new OrmWeather(54353L, 1L, "City1", new Date(), 17.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, ""));
        FORECAST.add(new OrmWeather(54353L, 2L, "City2", new Date(), 17.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, ""));
        FORECAST.add(new OrmWeather(54353L, 3L, "City3", new Date(), 17.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, ""));
    }

    @Test
    public void loadAllCitiesFromRepository() {
        Observable<List<OrmCity>> postsObservable = Observable.just(CITIES);
        when(mRepository.getCityList()).thenReturn(postsObservable);
        TestSubscriber<List<OrmCity>> testSubscriber = new TestSubscriber<>();
        mRepository.getCityList().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Collections.singletonList(CITIES));
    }

    @Test
    public void loadAllWeatherFromRepositoryHaveInternet() {
        Observable<List<OrmWeather>> postsObservable = Observable.just(FORECAST);

        when(mRepository.getForecast(54353, true)).thenReturn(postsObservable);

        when(mRepository.getForecast(54353, true)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRepository.getForecast(54353, true).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Collections.singletonList(FORECAST));
    }

    @Test
    public void loadAllWeatherFromRepositoryHaveNotInternet() {
        Observable<List<OrmWeather>> postsObservable = Observable.just(FORECAST);
        when(mRepository.getForecast(54353, false)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRepository.getForecast(54353, false).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Collections.singletonList(FORECAST));
    }

    @Test
    public void loadAllWeatherFromRepositoryNoData() {
        Observable<List<OrmWeather>> postsObservable = Observable.empty();
        when(mRepository.getForecast(54353, false)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRepository.getForecast(54353, false).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(new ArrayList<>());
    }
}
