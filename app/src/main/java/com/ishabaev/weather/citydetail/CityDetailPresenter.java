package com.ishabaev.weather.citydetail;

import android.os.AsyncTask;

import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.CityWithWeather;
import com.ishabaev.weather.data.Day;
import com.ishabaev.weather.data.source.DataSource;
import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.util.DataSort;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 18.06.16.
 */
public class CityDetailPresenter implements CityDetailContract.Presenter {

    private CityDetailContract.View mView;
    private Repository mRepository;
    private CompositeSubscription mSubscriptions;

    public CityDetailPresenter(CityDetailContract.View cityDetailView, Repository repository) {
        mView = cityDetailView;
        mRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void openCity(int cityId) {
        /*
        mRepository.getForecast(cityId, mView.isNetworkAvailable(),
                new DataSource.LoadWeatherCallback() {
                    @Override
                    public void onWeatherLoaded(List<OrmWeather> forecast) {
                        makeView(forecast);
                    }

                    @Override
                    public void onDataNotAvailable(Throwable t) {
                        t.printStackTrace();
                    }
                });
                */
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getForecast(cityId,mView.isNetworkAvailable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OrmWeather>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<OrmWeather> ormWeathers) {
                        makeView(ormWeathers);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void makeView(List<OrmWeather> forecast) {
        DataSort.sortWeatherHour(forecast);
        if (forecast.size() > 0) {
            OrmWeather current = forecast.get(0);
            setTemperature(current.getTemp());
            setHummidity(current.getHumidity());
            setWind(current.getWind_speed());
            setPressure(current.getPressure());
            setDate(current.getDt());
            mView.setImage(current.getIcon() + ".jpg");
            addDaysToViewPager(forecast);
        }

        for (OrmWeather weather : forecast) {
            weather.getTemp();
        }
    }


    private void setTemperature(Double temp) {
        String temperature = temp > 0 ? "+" + Integer.toString(temp.intValue()) : Integer.toString(temp.intValue());
        temperature += " °C";
        mView.setTemp(temperature);
    }

    private void setHummidity(double hummidity) {
        String value = "Влажность: " + Double.toString(hummidity);
        value += "%";
        mView.setHummidity(value);
    }

    private void setWind(Double wind) {
        String value = wind == null ? "No wind" : "Скорость ветра: " + Double.toString(wind) + " km\\h";
        mView.setWindSpeed(value);
    }

    private void setPressure(Double pressure) {
        String value = pressure == null ? "No pressure" : "Давление: " + Double.toString(pressure);
        mView.setPressure(value);
    }

    private void setDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM, HH:mm");
        String value = format.format(date);
        mView.setDate(value);
    }

    private void addDaysToViewPager(List<OrmWeather> hours) {
        List<Day> days = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(hours.get(0).getDt());
        Day day = new Day();
        //List<Weather> dayHours = new ArrayList<>();
        day.setHours(new ArrayList<OrmWeather>());
        days.add(day);
        for (OrmWeather hourWeayher : hours) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(hourWeayher.getDt());
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == 0) {
                day.getHours().add(hourWeayher);
            } else {
                //day.setHours(dayHours);
                //days.add(day);
                //dayHours = new ArrayList<>();
                //dayHours.add(hourWeayher);
                day = new Day();
                day.setHours(new ArrayList<OrmWeather>());
                day.getHours().add(hourWeayher);
                days.add(day);
            }
            c1 = c2;
        }
        mView.addDays(days);
    }
}
