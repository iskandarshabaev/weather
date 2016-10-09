package com.ishabaev.weather.citydetail;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.weather.Injection;
import com.ishabaev.weather.R;
import com.ishabaev.weather.data.model.Day;
import com.ishabaev.weather.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CityDetailFragment extends Fragment implements CityDetailContract.View {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    private CityDetailContract.Presenter mPresenter;
    private DaysViewPagerAdapter mViewPagerAdapter;
    private FrameLayout mProgressFrame;
    private TabLayout mTabLayout;
    private boolean mWaitAnimations;

    public CityDetailFragment() {

    }

    public static CityDetailFragment getInstance(long itemId, String itemName) {
        Bundle args = new Bundle();
        args.putLong(ARG_ITEM_ID, itemId);
        args.putString(ARG_ITEM_NAME, itemName);
        CityDetailFragment cityDetailFragment = new CityDetailFragment();
        cityDetailFragment.setArguments(args);
        return cityDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CityDetailPresenter(this, Injection.provideRepository(getContext()),
                Schedulers.io(), AndroidSchedulers.mainThread()
        );
        mPresenter.subscribe();
        if (savedInstanceState != null) {
            long cityId = getArguments().getLong(ARG_ITEM_ID);
            mPresenter.openCity((int) cityId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        FragmentManager fragmentManager = getChildFragmentManager();
        mViewPagerAdapter = new DaysViewPagerAdapter(fragmentManager, new ArrayList<>());
        viewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressFrame = (FrameLayout) getActivity().findViewById(R.id.progressFrame);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPagerAdapter.clear();
        mTabLayout.setVisibility(View.VISIBLE);
        if (!mWaitAnimations) {
            loadContent();
        }
    }

    public void waitAnimations() {
        mWaitAnimations = true;
    }

    public void loadContent() {
        long cityId = getArguments().getLong(ARG_ITEM_ID);
        mPresenter.openCity((int) cityId);
        mWaitAnimations = false;
    }

    public void clearContent() {
        mTabLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (mProgressFrame == null) {
            return;
        }
        if (show) {
            mProgressFrame.setVisibility(View.VISIBLE);
        } else {
            mProgressFrame.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTemp(int temperature) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_temp);
        if (view != null) {
            Resources res = getResources();
            String temperatureText = temperature > 0 ?
                    res.getString(R.string.temp_plus, temperature) :
                    res.getString(R.string.temp_minus, temperature);
            view.setText(temperatureText);
        }
    }

    @Override
    public void setHumidity(double humidity) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_humidity);
        if (view != null) {
            Resources res = getResources();
            String humidityText = res.getString(R.string.humidity, humidity);
            view.setText(humidityText);
        }
    }

    @Override
    public void setWindSpeed(double windSpeed) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_wind);
        if (view != null) {
            Resources res = getResources();
            String windText = res.getString(R.string.wind, windSpeed);
            view.setText(windText);
        }
    }

    @Override
    public void setPressure(double pressure) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_pressure);
        if (view != null) {
            Resources res = getResources();
            String pressureText = res.getString(R.string.pressure, pressure);
            view.setText(pressureText);
        }
    }

    @Override
    public void setDate(@NonNull String date) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_date);
        if (view != null) {
            view.setText(date);
        }
    }

    @Override
    public void setImage(@NonNull Drawable drawable) {
        ImageView view = (ImageView) getActivity().findViewById(R.id.backdrop);
        if (view != null) {
            view.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImage(@NonNull String assetName) {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.backdrop);
        if (imageView != null) {
            String fileName = assetName + ".jpg";
            ImageHelper.load("file:///android_asset/" + fileName, imageView);
        }
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void addDays(@NonNull List<Day> days) {
        mViewPagerAdapter.addDays(days);
    }

    @Override
    public void showNoForecast() {

    }

    @Override
    public void showError() {
        View view = getActivity().findViewById(R.id.viewpager);
        if (view != null) {
            String text = getResources().getString(R.string.error) + ": ";
            text += getResources().getString(R.string.failed_to_load_weather);
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
