package com.ishabaev.weather.addcity;

import android.content.res.Resources;
import android.text.Editable;

import com.ishabaev.weather.dao.OrmCity;

import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public interface AddCityContract {

    interface View{

        int getCitiesSize();

        void clearCities();

        void addCitiesToList(List<OrmCity> cities);

        void addCityToList(OrmCity city);

        void setImageViewVisibility(int visibility);

        void setProgressBarValue(int value);

        void setProgressBarVisibility(int visibility);

        void setSearchStateVisibility(int visibility);

        void setSearchStateText(String text);

        Resources getResources();
    }

    interface Presenter {

        void textChanged(Editable s);

        void onItemClick(OrmCity city);
    }
}
