package com.ishabaev.weather.citydetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ishabaev on 18.06.16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public String[] tabs = new String[]{
            "19 июня +17 °C",
            "20 июня +20 °C",
            "21 июня +16 °C",
            "22 июня +14 °C",
            "23 июня +12 °C",
    };

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
        public Fragment getItem(int position) {
            return new TabFragment();    // Which Fragment should be dislpayed by the viewpager for the given position
            // In my case we are showing up only one fragment in all the three tabs so we are
            // not worrying about the position and just returning the TabFragment
        }

        @Override
        public int getCount() {
            return tabs.length;           // As there are only 3 Tabs
        }
}
