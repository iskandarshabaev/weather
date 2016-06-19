package com.ishabaev.weather.citydetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ishabaev.weather.R;

public class CityDetailFragment extends Fragment implements CityDetailContract.View{

    public static final String ARG_ITEM_ID = "item_id";
    private CityDetailContract.UserActionsListener mUserActionsListener;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_ITEM_ID));
            }
        }
        mUserActionsListener = new CityDetailPresenter(this);
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

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }


    @Override
    public void onResume() {
        super.onResume();
        String cityName = getArguments().getString(ARG_ITEM_ID);
        mUserActionsListener.openCity(cityName);
    }

    @Override
    public void showProgressBar(boolean show) {
        if(show) {
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
