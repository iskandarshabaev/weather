package com.ishabaev.weather.citydetail;

import android.content.Context;
import android.os.AsyncTask;

import com.ishabaev.weather.dao.DaoMaster;
import com.ishabaev.weather.dao.DaoSession;
import com.ishabaev.weather.dao.Weather;
import com.ishabaev.weather.dao.WeatherDao;
import com.ishabaev.weather.data.City;
import com.ishabaev.weather.data.Day;
import com.ishabaev.weather.data.Forecast;
import com.ishabaev.weather.data.WeatherHour;
import com.ishabaev.weather.rest.ApiClient;
import com.ishabaev.weather.rest.OpenWeatherService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishabaev on 18.06.16.
 */
public class CityDetailPresenter implements CityDetailContract.UserActionsListener{

    private WeatherDao mWeatherDao;
    private OpenWeatherService mService = ApiClient.retrofit().create(OpenWeatherService.class);
    private CityDetailContract.View mCityDetailView;

    public CityDetailPresenter(CityDetailContract.View cityDetailView){
        mCityDetailView = cityDetailView;
    }

    public void initDao(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"db",null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        daoSession.clear();
        mWeatherDao = daoSession.getWeatherDao();
    }

    @Override
    public void openCity(int cityId) {

        //mWeatherDao.deleteAll();
        List<Weather> forecast = mWeatherDao.queryBuilder()
                .where(WeatherDao.Properties.City_id.eq(cityId))
                .orderAsc(WeatherDao.Properties.Dt)
                .list();

        if(forecast.size() == 0){
            OpenWeatherService service = ApiClient.retrofit().create(OpenWeatherService.class);
            service.getForecast(ApiClient.APPID,cityId,"metric")
                    .enqueue(new Callback<Forecast>() {
                        @Override
                        public void onResponse(Call<Forecast> call, Response<Forecast> response) {

                            if(response.body() == null){
                                return;
                            }

                            if(response.body().getList() == null){
                                return;
                            }

                            City city = response.body().getCity();
                            List<Weather> forecast = new ArrayList<>(response.body().getList().size());
                            for(WeatherHour hour : response.body().getList()){
                                Weather weather = new Weather();
                                weather.setCity_id(city.getId());
                                weather.setCity_name(city.getName());

                                weather.setDt(new Date(hour.getDt()*1000));
                                weather.setClouds(hour.getClouds().getAll());
                                weather.setHumidity(hour.getMain().getHumidity());
                                weather.setPressure(hour.getMain().getPressure());
                                weather.setTemp(hour.getMain().getTemp());
                                weather.setTemp_min(hour.getMain().getTempMin());
                                weather.setTemp_max(hour.getMain().getTempMax());
                                if(hour.getWind() != null){
                                    weather.setWind_speed(hour.getWind().getSpeed());
                                }
                                weather.setRain(hour.getRain() == null ? 0.0: hour.getRain().getVal());
                                weather.setSnow(hour.getSnow() == null ? 0.0: hour.getSnow().getVal());
                                forecast.add(weather);
                            }

                            mWeatherDao.insertInTx(forecast);
                            makeView(forecast);
                        }

                        @Override
                        public void onFailure(Call<Forecast> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }else {
            makeView(forecast);
        }
        /*
        ProgressBarTestTask progressBarTestTask = new ProgressBarTestTask();
        progressBarTestTask.execute();
        */
    }

    private void makeView(List<Weather> forecast){

        sortWeatherHour(forecast);

        if(forecast.size() > 0){
            Weather current = forecast.get(0);
            setTemperature(current.getTemp());
            setHummidity(current.getHumidity());
            setWind(current.getWind_speed());
            setDate(current.getDt());
            addDaysToViewPager(forecast);
        }

        for(Weather weather : forecast){
            weather.getTemp();
        }
    }

    private void sortWeatherHour(List<Weather> forecast){
        Collections.sort(forecast, new Comparator<Weather>() {
            @Override
            public int compare(Weather o1, Weather o2) {
                return o1.getDt().compareTo(o2.getDt());
            }
        });
    }

    private void setTemperature(Double temp){
        String temperature = temp > 0 ? "+" + Integer.toString(temp.intValue()) : Integer.toString(temp.intValue());
        temperature += " °C";
        mCityDetailView.setTemp(temperature);
    }

    private void setHummidity(double hummidity){
        String value = "Влажность: " + Double.toString(hummidity);
        value += "%";
        mCityDetailView.setHummidity(value);
    }

    private void setWind(Double wind){
        String value = wind == null ? "No wind" : "Скорость ветра: " + Double.toString(wind) + " km\\h";
        mCityDetailView.setWindSpeed(value);
    }

    private void setDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM, HH:mm");
        String value = format.format(date);
        mCityDetailView.setDate(value);
    }

    private void addDaysToViewPager(List<Weather> hours){
        List<Day> days = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(hours.get(0).getDt());
        Day day = new Day();
        //List<Weather> dayHours = new ArrayList<>();
        day.setHours(new ArrayList<Weather>());
        days.add(day);
        for(Weather hourWeayher : hours){
            Calendar c2 = Calendar.getInstance();
            c2.setTime(hourWeayher.getDt());
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == 0) {
                day.getHours().add(hourWeayher);
            }else {
                //day.setHours(dayHours);
                //days.add(day);
                //dayHours = new ArrayList<>();
                //dayHours.add(hourWeayher);
                day = new Day();
                day.setHours(new ArrayList<Weather>());
                day.getHours().add(hourWeayher);
                days.add(day);
            }
            c1 = c2;
        }
        mCityDetailView.addDays(days);
    }

    private class ProgressBarTestTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCityDetailView.showProgressBar(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCityDetailView.showProgressBar(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            }catch (InterruptedException exception){
                exception.printStackTrace();
            }
            return null;
        }
    }
}
