package com.ishabaev.weather.cities;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.RepositoryDataSource;
import com.ishabaev.weather.data.source.model.CityWithWeather;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ishabaev on 27.07.16.
 */
public class CitiesPresenterTest {

    private static List<OrmCity> CITIES;

    @Mock
    private RepositoryDataSource mRepository;

    @Mock
    private CitiesContract.View mView;

    private CitiesPresenter mPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new CitiesPresenter(mView, mRepository, Schedulers.immediate(), Schedulers.immediate());
        when(mView.isActive()).thenReturn(true);
        mPresenter.subscribe();
        CITIES = new ArrayList<>();
        CITIES.add(new OrmCity(54353L, "City1", "RU", 55.4, 34.4));
        //CITIES.add(new OrmCity(54354L, "City2", "RU", 55.4, 34.4));
        //CITIES.add(new OrmCity(54355L, "City3", "RU", 55.4, 34.4));
    }

    @Test
    public void loadCities() {
        when(mRepository.getCityList()).thenReturn(Observable.just(CITIES));
        Observable<OrmWeather> oWeather = Observable.just(new OrmWeather());
        when(mRepository.getSingleForecast(CITIES.get(0).get_id().intValue(), false)).thenReturn(oWeather);
        mPresenter.loadCities();
        verify(mView).setRefreshing(true);
        verify(mRepository).getCityList();
        ArgumentCaptor<CityWithWeather> showTasksArgumentCaptor = ArgumentCaptor.forClass(CityWithWeather.class);
        verify(mView).addCityToList(showTasksArgumentCaptor.capture());
        verify(mView).setRefreshing(false);
    }

    @Test
    public void removeCity() {
        mRepository.deleteCity(CITIES.get(0));
    }

    @Test
    public void removeWeather() {
        mRepository.deleteForecast(CITIES.get(0).get_id().intValue());
    }
}
