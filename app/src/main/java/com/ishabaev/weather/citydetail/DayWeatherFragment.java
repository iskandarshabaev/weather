package com.ishabaev.weather.citydetail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmWeather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ishabaev on 18.06.16.
 */
public class DayWeatherFragment extends Fragment implements DayWeatherContract.View {

    public final static String CITY_ID = "city_id";
    public final static String DATE = "date";
    private HoursRecyclerViewAdapter mAdapter;
    private DayWeatherContract.Presenter mPresenter;

    public DayWeatherFragment() {

    }

    public static DayWeatherFragment getInstance(int cityId, long time) {
        Bundle args = new Bundle();
        args.putInt(CITY_ID, cityId);
        args.putLong(DATE, time);
        DayWeatherFragment dayWeatherFragment = new DayWeatherFragment();
        dayWeatherFragment.setArguments(args);
        return dayWeatherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hour_list);
        setupRecyclerView(recyclerView);
        mPresenter = new DayWeatherPresenter(this, Injection.provideTasksRepository(getContext()));
        mPresenter.loadDayForecast(getArguments().getInt(CITY_ID),
                new Date(getArguments().getLong(DATE)));
        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new HoursRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addWeathersToList(List<OrmWeather> weatherList) {
        mAdapter.addElements(weatherList);
    }

    @Override
    public void addWeatherToList(OrmWeather weather) {
        mAdapter.addElement(weather);
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }
}