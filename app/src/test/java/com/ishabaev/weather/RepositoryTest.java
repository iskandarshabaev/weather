package com.ishabaev.weather;

import android.content.res.AssetManager;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.DataSource;
import com.ishabaev.weather.data.source.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * Created by ishabaev on 03.07.16.
 */
public class RepositoryTest {

    private static List<OrmCity> CITIES;
    private static List<OrmWeather> FORECAST;

    @Mock
    private Repository mRespository;

    @Before
    public void setupData() {
        MockitoAnnotations.initMocks(this);
        initCities();
        initForecats();
    }

    private void initCities(){
        CITIES = new ArrayList<OrmCity>();
        CITIES.add(new OrmCity(54353l,"City1", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54354l,"City2", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54355l,"City3", "RU", 55.4, 34.4));
    }

    private void initForecats(){
        FORECAST = new ArrayList<OrmWeather>();
        FORECAST.add(new OrmWeather(54353l,1l,"City1", new Date(),17.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,""));
        FORECAST.add(new OrmWeather(54353l,2l,"City2", new Date(),17.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,""));
        FORECAST.add(new OrmWeather(54353l,3l,"City3", new Date(),17.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,15.0,""));
    }

    @Test
    public void loadAllCitiesFromRepository() {
        Observable<List<OrmCity>> postsObservable = Observable.just(CITIES);
        when(mRespository.getCityList()).thenReturn(postsObservable);
        TestSubscriber<List<OrmCity>> testSubscriber = new TestSubscriber<>();
        mRespository.getCityList().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(CITIES));
    }

    @Test
    public void loadAllWeatherFromRepositoryHaveInternet() {
        Observable<List<OrmWeather>> postsObservable = Observable.just(FORECAST);

        when(mRespository.getForecast(54353, true)).thenReturn(postsObservable);

        when(mRespository.getForecast(54353, true)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRespository.getForecast(54353, true).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(FORECAST));
    }

    @Test
    public void loadAllWeatherFromRepositoryHaveNotInternet() {
        Observable<List<OrmWeather>> postsObservable = Observable.just(FORECAST);
        when(mRespository.getForecast(54353, false)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRespository.getForecast(54353, false).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(FORECAST));
    }

    @Test
    public void loadAllWeatherFromRepositoryNoData() {
        Observable<List<OrmWeather>> postsObservable = Observable.empty();
        when(mRespository.getForecast(54353, false)).thenReturn(postsObservable);
        TestSubscriber<List<OrmWeather>> testSubscriber = new TestSubscriber<>();
        mRespository.getForecast(54353, false).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(new ArrayList<List<OrmWeather>>());
    }
}
