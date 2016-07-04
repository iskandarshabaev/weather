package com.ishabaev.weather.addcity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.cities.CitiesActivity;
import com.ishabaev.weather.dao.OrmCity;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity implements AddCityContract.View {

    private AddCityViewAdapter mAdapter;
    private TextView mTextView;
    private Handler mHandler;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private TextView mSearchState;
    private ImageView mImageView;
    private AddCityContract.Presenter mPresenter;
    private final static int LINE_SIZE = 74062;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mPresenter = new AddCityPresenter(this, Injection.provideTasksRepository(this));
        mRecyclerView = (RecyclerView) findViewById(R.id.city_search_list);
        setupRecyclerView(mRecyclerView);
        mSearchState = (TextView) findViewById(R.id.textView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(LINE_SIZE);
        mProgressBar.setVisibility(View.GONE);
        mHandler = new Handler();
        mTextView = (TextView) findViewById(R.id.editText);
        mTextView.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mPresenter.textChanged(s);
                    }
                }
        );
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        mAdapter = new AddCityViewAdapter(new ArrayList<OrmCity>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new AddCityViewAdapter.AddCityRecyclerViewItemListener() {
            @Override
            public void onItemClick(OrmCity city) {
                mPresenter.onItemClick(city);
                Intent intent = new Intent(AddCityActivity.this, CitiesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
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
    public void setImageViewVisibility(int visibility) {
        mImageView.setVisibility(visibility);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    @Override
    public void setSearchStateVisibility(int visibility) {
        mSearchState.setVisibility(visibility);
    }

    @Override
    public void setSearchStateText(String text) {
        mSearchState.setText(text);
    }

    @Override
    public void setProgressBarValue(int value) {
        mProgressBar.setProgress(value);
    }
}
