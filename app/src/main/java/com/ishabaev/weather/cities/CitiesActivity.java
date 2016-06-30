package com.ishabaev.weather.cities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.addcity.AddCityActivity;
import com.ishabaev.weather.citydetail.CityDetailActivity;
import com.ishabaev.weather.citydetail.CityDetailFragment;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity implements CitiesContract.View {

    private boolean mTwoPane;
    private CitiesContract.Presenter mPresenter;
    private CitiesRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                    Intent intent = new Intent(CitiesActivity.this, AddCityActivity.class);
                    startActivity(intent);
                }
            });
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadCities();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mPresenter = new CitiesPresenter(this, Injection.provideTasksRepository(getApplicationContext()));
        mPresenter.loadCities();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void setPresenter(CitiesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        mAdapter = new CitiesRecyclerViewAdapter(new ArrayList<CityWithWeather>());
        mAdapter.setListener(listener);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int location = viewHolder.getAdapterPosition();
                mPresenter.removeWeaher(mAdapter.getCity(location).get_id().intValue());
                mPresenter.removeCity(mAdapter.getCity(location));
                mAdapter.removeCity(location);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setCities(List<CityWithWeather> cities) {
        mAdapter.setCities(cities);
    }

    @Override
    public void addCitiesToList(List<CityWithWeather> cities) {
        mAdapter.addCities(cities);
    }

    @Override
    public void addCityToList(CityWithWeather cityWithWeather) {
        mAdapter.addCity(cityWithWeather);
    }
}
