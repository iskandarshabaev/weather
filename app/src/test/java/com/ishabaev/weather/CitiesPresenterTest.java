package com.ishabaev.weather;

import android.content.Context;
import android.content.res.AssetManager;

import com.ishabaev.weather.cities.CitiesContract;
import com.ishabaev.weather.cities.CitiesPresenter;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.source.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ishabaev on 30.06.16.
 */
public class CitiesPresenterTest {

    private static List<OrmCity> CITIES;

    @Mock
    private Repository mRespository;

    @Mock
    private Context mContext;

    @Mock
    private CitiesContract.View mView;

    private CitiesPresenter mTasksPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mTasksPresenter = new CitiesPresenter(mView, mRespository, Schedulers.immediate(),Schedulers.immediate());
        when(mView.isActive()).thenReturn(true);
        CITIES = new ArrayList<OrmCity>();
        CITIES.add(new OrmCity(54353L,"City1", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54354L,"City2", "RU", 55.4, 34.4));
        CITIES.add(new OrmCity(54355L,"City3", "RU", 55.4, 34.4));
    }

    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {


        mTasksPresenter.loadCities();
        /*
        Observable<List<OrmCity>> postsObservable = Observable.just(CITIES);
        when(mRespository.getCityList()).thenReturn(postsObservable);

        mTasksPresenter.loadCities();

        mRespository.getCityList();
        */
    }
}
