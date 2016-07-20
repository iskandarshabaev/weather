package com.ishabaev.weather.citydetail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ishabaev.weather.R;

public class CityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(CityDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(CityDetailFragment.ARG_ITEM_ID, 0));
            arguments.putString(CityDetailFragment.ARG_ITEM_NAME,
                    getIntent().getStringExtra(CityDetailFragment.ARG_ITEM_NAME));
            CityDetailFragment fragment = new CityDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit();

        }
        String cityName = getIntent().getStringExtra(CityDetailFragment.ARG_ITEM_NAME);
        if (cityName != null) {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cityName);
            } else if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(cityName);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        supportFinishAfterTransition();
        return false;
    }
}
