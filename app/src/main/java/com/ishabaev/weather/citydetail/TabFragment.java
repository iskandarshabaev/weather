package com.ishabaev.weather.citydetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.weather.R;
import com.ishabaev.weather.cities.CitiesRecyclerViewAdapter;
import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.dao.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishabaev on 18.06.16.
 */
public class TabFragment extends Fragment {

    private View mView;
    private HoursRecyclerViewAdapter mAdapter;
    private List<Weather> mHours;

    public TabFragment(){
        Bundle args=new Bundle();
        this.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tabs, container, false);
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.hour_list);
        setupRecyclerView(recyclerView);
        return mView;
    }

    public void setHours(List<Weather> hours){
        mHours = hours;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new HoursRecyclerViewAdapter(mHours);
        recyclerView.setAdapter(mAdapter);

    }
}