package com.ishabaev.weather.citydetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ishabaev.weather.R;
import com.ishabaev.weather.cities.CitiesActivity;

/**
 * An activity representing a single City detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CitiesActivity}.
 */
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
        }
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(CityDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(CityDetailFragment.ARG_ITEM_ID,0));
            arguments.putString(CityDetailFragment.ARG_ITEM_NAME,
                    getIntent().getStringExtra(CityDetailFragment.ARG_ITEM_NAME));
            CityDetailFragment fragment = new CityDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit();

        }
        //CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //appBarLayout.setTitle(getIntent().getStringExtra(CityDetailFragment.ARG_ITEM_ID));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, CitiesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
