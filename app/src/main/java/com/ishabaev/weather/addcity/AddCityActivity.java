package com.ishabaev.weather.addcity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishabaev.weather.EspressoIdlingResource;
import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.rxview.RxEditText;
import com.ishabaev.weather.cities.CitiesActivity;
import com.ishabaev.weather.dao.OrmCity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCityActivity extends AppCompatActivity implements AddCityContract.View {

    public final static int debounce = 500;
    private AddCityViewAdapter mAdapter;
    private RxEditText mTextView;
    private ProgressBar mProgressBar;
    private TextView mSearchState;
    private ImageView mImageView;
    private AddCityContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mPresenter = new AddCityPresenter(this, Injection.provideRepository(this),
                Injection.provideFileSource(this),Schedulers.io(), AndroidSchedulers.mainThread());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_search_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        mSearchState = (TextView) findViewById(R.id.textView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        assert mProgressBar != null;
        mProgressBar.setVisibility(View.GONE);
        mTextView = (RxEditText) findViewById(R.id.editText);
        assert mTextView != null;
        mTextView.setOnRxTextChangeListener(
                this::textChanged,
                debounce);
    }

    private void textChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            clearCities();
            setImageViewVisible(true);
            setSearchStateVisible(true);
            showStartTyping();
        } else {
            mPresenter.textChanged(text);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        mAdapter = new AddCityViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(city -> {
            mPresenter.onItemClick(city);
            Intent intent = new Intent(AddCityActivity.this, CitiesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTextView.getWindowToken(), 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        hideKeyboard();
        onBackPressed();
        return true;
    }

    @Override
    public int getCitiesSize() {
        return mAdapter.getItemCount();
    }

    @Override
    public void clearCities() {
        mAdapter.clear();
    }

    @Override
    public void addCitiesToList(List<OrmCity> cities) {
        mAdapter.addCities(cities);
    }

    @Override
    public void addCityToList(OrmCity city) {
        mAdapter.addCity(city);
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setImageViewVisible(boolean visible) {
        if (visible) {
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSearchStateVisible(boolean visible) {
        if (visible) {
            mSearchState.setVisibility(View.VISIBLE);
        } else {
            mSearchState.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCouldNotFindCity() {
        mSearchState.setText(getResources().getString(R.string.could_not_find_a_city));
    }

    @Override
    public void showStartTyping() {
        mSearchState.setText(getResources().getString(R.string.start_typing));
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
