package com.ishabaev.weather.addcity;

import android.content.res.Resources;

import com.ishabaev.weather.BasePresenter;
import com.ishabaev.weather.BaseView;
import com.ishabaev.weather.dao.OrmCity;

import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public interface AddCityContract {

    interface View extends BaseView {

        int getCitiesSize();

        void clearCities();

        void addCitiesToList(List<OrmCity> cities);

        void addCityToList(OrmCity city);

        void setImageViewVisible(boolean visible);

        void setProgressBarVisibile(boolean visible);

        void setSearchStateVisibile(boolean visible);

        void showStartTyping();

        void showCouldNotFindCity();

        Resources getResources();
    }

    interface Presenter extends BasePresenter {

        //void textChanged(Editable s);

        void textChanged(String s);

        void onItemClick(OrmCity city);
    }
}
