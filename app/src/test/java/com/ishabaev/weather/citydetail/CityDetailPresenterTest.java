package com.ishabaev.weather.citydetail;

import android.content.res.Resources;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.RepositoryDataSource;
import com.ishabaev.weather.data.model.Day;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ishabaev on 28.07.16.
 */
public class CityDetailPresenterTest {

    private static List<OrmWeather> WEATHER_LIST;

    @Mock
    private RepositoryDataSource mRepository;

    @Mock
    private CityDetailContract.View mView;

    @Mock
    private Resources mResources;

    private CityDetailPresenter mPresenter;

    private final static long cityId = 54353;

    @Captor
    private ArgumentCaptor<List<Day>> days;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new CityDetailPresenter(mView, mRepository, Schedulers.immediate(), Schedulers.immediate());
        when(mView.isActive()).thenReturn(true);
        mPresenter.subscribe();
        WEATHER_LIST = makeWeatherList();
    }

    private List<OrmWeather> makeWeatherList() {
        List<OrmWeather> weatherList = new ArrayList<>();
        OrmWeather ormWeather = new OrmWeather();
        ormWeather.setCity_id(cityId);
        ormWeather.setCity_name("CityName");
        ormWeather.setDt(new Date());
        ormWeather.setHumidity(5.5);
        ormWeather.setTemp(5.5);
        weatherList.add(ormWeather);
        return weatherList;
    }

    @Test
    public void openCityWithEmptyForecast() {
        when(mRepository.getForecast(54353, false)).thenReturn(Observable.just(new ArrayList<>()));
        when(mView.getResources()).thenReturn(mResources);
        when(mResources.getString(R.string.error)).thenReturn("Error");
        mPresenter.openCity(54353);
        verify(mRepository).getForecast(54353, false);
        ArgumentCaptor<String> errorMessage = ArgumentCaptor.forClass(String.class);
        verify(mView).showSnackBar(errorMessage.capture());
        verify(mView).showProgressBar(false);
    }

    @Test
    public void openCityWith() {
        when(mRepository.getForecast((int) cityId, false)).thenReturn(Observable.just(WEATHER_LIST));
        when(mView.getResources()).thenReturn(mResources);
        mPresenter.openCity((int) cityId);
        verify(mRepository).getForecast((int) cityId, false);
        verify(mView).showProgressBar(false);
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(mView).setTemp(text.capture());
        verify(mView).setHumidity(text.capture());
        verify(mView).setWindSpeed(text.capture());
        verify(mView).setPressure(text.capture());
        verify(mView).setDate(text.capture());
        verify(mView).addDays(days.capture());
    }
}
