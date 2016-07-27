package com.ishabaev.weather.cities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.addcity.AddCityActivity;
import com.ishabaev.weather.citydetail.CityDetailActivity;
import com.ishabaev.weather.citydetail.CityDetailFragment;
import com.ishabaev.weather.data.source.model.CityWithWeather;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CitiesActivity extends AppCompatActivity implements CitiesContract.View {

    private boolean mTwoPane;
    private CitiesContract.Presenter mPresenter;
    private CitiesRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static String FRAGMENT_TAG = "fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(CitiesActivity.this, AddCityActivity.class);
                startActivity(intent);
            });
        }

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadCities());

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mPresenter = new CitiesPresenter(this, Injection.provideTasksRepository(getApplicationContext()),
                Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(refreshing));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int spanCount;
        int orientation = getResources().getConfiguration().orientation;
        if (!mTwoPane && Configuration.ORIENTATION_LANDSCAPE == orientation) {
            spanCount = 2;
        } else {
            spanCount = 1;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        mAdapter = new CitiesRecyclerViewAdapter(this, new ArrayList<>());
        mAdapter.setListener(mListener);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int location = viewHolder.getAdapterPosition();
                        mPresenter.removeWeaher(mAdapter.getCity(location).get_id().intValue());
                        mPresenter.removeCity(mAdapter.getCity(location));
                        mAdapter.removeCity(location);
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                        if (mAdapter.getCurrentPosition() == location && mTwoPane && fragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .remove(fragment)
                                    .commit();
                            mAdapter.setCurrentPosition(-1);
                            showNoCitiesFrameIfNeed();
                        } else if (mAdapter.getCurrentPosition() > location) {
                            mAdapter.setCurrentPosition(mAdapter.getCurrentPosition() - 1);
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private CitiesRecyclerViewAdapter.CitiesRecyclerViewItemListener mListener =
            new CitiesRecyclerViewAdapter.CitiesRecyclerViewItemListener() {

                @Override
                public void onItemClick(CityWithWeather city, View view) {
                    if (mTwoPane) {
                        long cityId = city.getCity().get_id();
                        String cityName = city.getCity().getCity_name();
                        CityDetailFragment fragment = CityDetailFragment.getInstance(cityId, cityName);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment, FRAGMENT_TAG)
                                .commit();
                        showNoCitiesFrameIfNeed();
                    } else {
                        Intent intent = new Intent(CitiesActivity.this, CityDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putLong(CityDetailFragment.ARG_ITEM_ID, city.getCity().get_id());
                        args.putString(CityDetailFragment.ARG_ITEM_NAME, city.getCity().getCity_name());
                        args.putString(CityDetailActivity.IMAGE_NAME, city.getWeather().getIcon());
                        /*
                        if (getResources().getConfiguration().orientation == OrientationHelper.VERTICAL &&
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            args.putString(CityDetailActivity.TRANSITION_NAME, view.getTransitionName());
                            intent.putExtras(args);
                            ActivityOptions transitionActivityOptions =
                                    ActivityOptions.makeSceneTransitionAnimation(CitiesActivity.this, view, view.getTransitionName());
                            ActivityCompat.startActivity(CitiesActivity.this, intent, transitionActivityOptions.toBundle());
                        } else {
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                        */
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
        mPresenter.loadCities();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setCities(List<CityWithWeather> cities) {
        mAdapter.setCities(cities);
        showNoCitiesFrameIfNeed();
    }

    private void showNoCitiesFrameIfNeed() {
        if (!mTwoPane) {
            return;
        }
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.selectCityFrame);
        if (mAdapter.getCurrentPosition() == -1) {
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void addCitiesToList(List<CityWithWeather> cities) {
        mAdapter.addCities(cities);
        showNoCitiesFrameIfNeed();
    }

    @Override
    public void addCityToList(CityWithWeather cityWithWeather) {
        mAdapter.addCity(cityWithWeather);
        showNoCitiesFrameIfNeed();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showSnackBar(String text) {
        View view = findViewById(R.id.fab);
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        }
    }
}
