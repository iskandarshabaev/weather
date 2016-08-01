package com.ishabaev.weather.addcity;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.source.FileManager;
import com.ishabaev.weather.data.source.RepositoryDataSource;

import org.junit.Before;
import org.junit.Test;
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
public class AddCityPresenterTest {

    private static List<OrmCity> CITIES;

    @Mock
    private RepositoryDataSource mRepository;

    @Mock
    private FileManager mFileManager;

    @Mock
    private AddCityContract.View mView;

    private AddCityPresenter mPresenter;

    private final static String cityName = "City1";

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new AddCityPresenter(mView, mRepository, mFileManager,
                Schedulers.immediate(), Schedulers.immediate());
        mPresenter.subscribe();
        CITIES = new ArrayList<>();
        CITIES.add(new OrmCity(54353L, cityName, "RU", 55.4, 34.4));
        //CITIES.add(new OrmCity(54354L, "City2", "RU", 55.4, 34.4));
        //CITIES.add(new OrmCity(54355L, "City3", "RU", 55.4, 34.4));
    }

    @Test
    public void onTextChanged() {
        when(mFileManager.searchCity(cityName)).thenReturn(Observable.just(CITIES.get(0)));
        mPresenter.textChanged(cityName);
    }

    @Test
    public void onItemClick() {
        mPresenter.onItemClick(CITIES.get(0));
        verify(mRepository).saveCity(CITIES.get(0));
    }
}
