package com.ishabaev.weather.cities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ishabaev.weather.R;
import com.ishabaev.weather.citydetail.CityDetailActivity;
import com.ishabaev.weather.citydetail.CityDetailFragment;
import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity implements CitiesContract.View{

    private boolean mTwoPane;
    private CitiesPresenter mPresenter;
    private CitiesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }

        mPresenter = new CitiesPresenter(this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new CitiesRecyclerViewAdapter(new ArrayList<CityWithWeather>());
        mAdapter.setListener(listener);
        recyclerView.setAdapter(mAdapter);
    }

    CitiesRecyclerViewAdapter.CitiesRecyclerViewItemListener listener = new CitiesRecyclerViewAdapter.CitiesRecyclerViewItemListener() {
        @Override
        public void onItemClick(CityWithWeather city) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putLong(CityDetailFragment.ARG_ITEM_ID, city.getCity().get_id());
                arguments.putString(CityDetailFragment.ARG_ITEM_NAME, city.getCity().getCity_name());
                CityDetailFragment fragment = new CityDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.city_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(CitiesActivity.this, CityDetailActivity.class);
                Bundle args = new Bundle();
                args.putLong(CityDetailFragment.ARG_ITEM_ID, city.getCity().get_id());
                args.putString(CityDetailFragment.ARG_ITEM_NAME, city.getCity().getCity_name());
                intent.putExtras(args);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.initDao(this);
        mAdapter.clear();
        mPresenter.loadCities();
    }

    @Override
    public void addCitiesToList(List<CityWithWeather> cities) {
        mAdapter.addCities(cities);
    }
}
