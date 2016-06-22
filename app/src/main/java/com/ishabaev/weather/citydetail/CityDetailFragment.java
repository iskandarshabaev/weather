package com.ishabaev.weather.citydetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.data.Day;

import java.util.ArrayList;
import java.util.List;

public class CityDetailFragment extends Fragment implements CityDetailContract.View{

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    private CityDetailContract.UserActionsListener mUserActionsListener;
    private DaysViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_ITEM_NAME));
            }
        }
        mUserActionsListener = new CityDetailPresenter(this);
        mUserActionsListener.initDao(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        FragmentManager fragmentManager = getChildFragmentManager();
        mViewPagerAdapter = new DaysViewPagerAdapter(fragmentManager, new ArrayList<Day>());
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    public void onResume() {
        super.onResume();
        long cityId = getArguments().getLong(ARG_ITEM_ID);
        mUserActionsListener.openCity((int)cityId);
    }

    @Override
    public void showProgressBar(boolean show) {
        /*
        if(show) {
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            mProgressBar.setVisibility(View.GONE);
        }
        */
    }

    @Override
    public void setTemp(String temperature) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_temp);
        if(view != null) {
            view.setText(temperature);
        }
    }

    @Override
    public void setHummidity(String hummidity) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_hummidity);
        if(view != null) {
            view.setText(hummidity);
        }
    }

    @Override
    public void setWindSpeed(String windSpeed) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_wind);
        if(view != null) {
            view.setText(windSpeed);
        }
    }

    @Override
    public void setDate(String date) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_date);
        if(view != null) {
            view.setText(date);
        }
    }

    @Override
    public void addDays(List<Day> days) {
        mViewPagerAdapter.addDays(days);
    }
}
