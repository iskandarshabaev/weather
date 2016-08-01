package com.ishabaev.weather.citydetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ishabaev.weather.data.source.model.Day;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by ishabaev on 18.06.16.
 */
public class DaysViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Day> mDays;

    public DaysViewPagerAdapter(FragmentManager fm, List<Day> days) {
        super(fm);
        mDays = days;
    }

    public void addDay(Day day) {
        mDays.add(day);
        notifyDataSetChanged();
    }

    public void addDays(List<Day> days) {
        mDays.addAll(days);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd E",Locale.getDefault());
        return format.format(mDays.get(position).getHours().get(0).getDt());
    }

    @Override
    public Fragment getItem(int position) {
        int cityId = mDays.get(position).getHours().get(0).getCity_id().intValue();
        long time = mDays.get(position).getHours().get(0).getDt().getTime();
        return DayWeatherFragment.getInstance(cityId, time);
    }

    @Override
    public int getCount() {
        return mDays.size();
    }

    public void clear() {
        mDays.clear();
        notifyDataSetChanged();
    }
}
