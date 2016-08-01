package com.ishabaev.weather.citydetail;

import android.content.res.Resources;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.source.RepositoryDataSource;

import org.junit.Before;
import org.junit.Test;
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
public class DayWeatherPresenterTest {

    private static List<OrmWeather> WEATHER_LIST;

    @Mock
    private RepositoryDataSource mRepository;

    @Mock
    private DayWeatherContract.View mView;

    @Mock
    private Resources mResources;

    private DayWeatherPresenter mPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new DayWeatherPresenter(mView, mRepository, Schedulers.immediate(), Schedulers.immediate());
        //when(mView.isActive()).thenReturn(true);
        mPresenter.subscribe();
        WEATHER_LIST = makeWeatherList();
    }

    private List<OrmWeather> makeWeatherList() {
        List<OrmWeather> weatherList = new ArrayList<>();
        OrmWeather ormWeather = new OrmWeather();
        ormWeather.setCity_id(54353L);
        ormWeather.setCity_name("CityName");
        ormWeather.setDt(new Date());
        ormWeather.setHumidity(5.5);
        ormWeather.setTemp(5.5);
        weatherList.add(ormWeather);
        return weatherList;
    }

    @Test
    public void loadDayForecast() {
        when(mRepository.getForecast(WEATHER_LIST.get(0).getCity_id().intValue(),
                WEATHER_LIST.get(0).getDt(), false))
                .thenReturn(Observable.just(WEATHER_LIST));

        mPresenter.loadDayForecast(WEATHER_LIST.get(0).getCity_id().intValue(),
                WEATHER_LIST.get(0).getDt());
        verify(mView).addWeathersToList(WEATHER_LIST);
    }

    /*@Test
    public void loadDayForecast() {
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
        ArgumentCaptor<List> days = ArgumentCaptor.forClass(List.class);
        verify(mView).addDays(days.capture());
    }*/
}
