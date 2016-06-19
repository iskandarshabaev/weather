package com.ishabaev.weather.citydetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.weather.R;

/**
 * Created by ishabaev on 18.06.16.
 */
public class TabFragment extends Fragment {

    private View mView;

    public TabFragment(){
        Bundle args=new Bundle();
        this.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tabs, container, false);
        return mView;
    }
}