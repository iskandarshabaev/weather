package com.ishabaev.weather.citydetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.ishabaev.weather.data.Day;
import com.ishabaev.weather.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class CityDetailFragment extends Fragment implements CityDetailContract.View {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    private CityDetailContract.Presenter mPresenter;
    private DaysViewPagerAdapter mViewPagerAdapter;
    private ImageUtils mImageUtils;
    private FrameLayout mProgressFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CityDetailPresenter(this, Injection.provideTasksRepository(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);
        mImageUtils = new ImageUtils(rootView.getContext());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressFrame = (FrameLayout) getActivity().findViewById(R.id.progressFrame);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        long cityId = getArguments().getLong(ARG_ITEM_ID);
        mViewPagerAdapter.clear();
        mPresenter.openCity((int) cityId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(CityDetailContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void setTemp(String temperature) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_temp);
        if (view != null) {
            view.setText(temperature);
        }
    }

    @Override
    public void setHummidity(String hummidity) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_hummidity);
        if (view != null) {
            view.setText(hummidity);
        }
    }

    @Override
    public void setWindSpeed(String windSpeed) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_wind);
        if (view != null) {
            view.setText(windSpeed);
        }
    }

    @Override
    public void setPressure(String pressure) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_pressure);
        if (view != null) {
            view.setText(pressure);
        }
    }

    @Override
    public void setDate(String date) {
        TextView view = (TextView) getActivity().findViewById(R.id.header_date);
        if (view != null) {
            view.setText(date);
        }
    }

    @Override
    public void setImage(Drawable drawable) {
        ImageView view = (ImageView) getActivity().findViewById(R.id.backdrop);
        if (view != null) {
            view.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImage(String assetName) {
        ImageView view = (ImageView) getActivity().findViewById(R.id.backdrop);
        if (view != null) {
            Bitmap bitmap = mImageUtils.decodeSampledBitmapFromAssets(assetName, 200, 200);
            if (bitmap != null) {
                view.setImageBitmap(bitmap);
            }
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
    public void addDays(List<Day> days) {
        mViewPagerAdapter.addDays(days);
    }

    @Override
    public void showNoForecast() {

    }

    @Override
    public void showSnackBar(String text) {
        View view = getActivity().findViewById(R.id.viewpager);
        if(view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        }
    }
}
