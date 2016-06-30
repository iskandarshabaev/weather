package com.ishabaev.weather.data.source.remote;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.City;
import com.ishabaev.weather.data.Forecast;
import com.ishabaev.weather.data.WeatherHour;
import com.ishabaev.weather.data.source.DataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ishabaev on 24.06.16.
 */
public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;
    private OpenWeatherService mService; // = ApiClient.retrofit().create(OpenWeatherService.class);

    private RemoteDataSource(OpenWeatherService service) {
        mService = service;
    }

    public static RemoteDataSource getInstance(OpenWeatherService service) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(service);
        }
        return INSTANCE;
    }

    @Override
    public void getForecast(int cityId, boolean isNetworkAvailable, final LoadWeatherCallback callback) {
        mService.getForecast(ApiClient.APPID, cityId, "metric")
                .enqueue(new Callback<Forecast>() {
                    @Override
                    public void onResponse(Call<Forecast> call, Response<Forecast> response) {

                        if (response.body() == null) {
                            return;
                        }

                        if (response.body().getList() == null) {
                            return;
                        }

                        City city = response.body().getCity();
                        List<OrmWeather> forecast = new ArrayList<>(response.body().getList().size());
                        for (WeatherHour hour : response.body().getList()) {
                            OrmWeather weather = new OrmWeather();
                            weather.setCity_id(city.getId());
                            weather.setCity_name(city.getName());

                            weather.setDt(new Date(hour.getDt() * 1000));
                            weather.setClouds(hour.getClouds().getAll());
                            weather.setHumidity(hour.getMain().getHumidity());
                            weather.setPressure(hour.getMain().getPressure());
                            weather.setTemp(hour.getMain().getTemp());
                            weather.setTemp_min(hour.getMain().getTempMin());
                            weather.setTemp_max(hour.getMain().getTempMax());
                            weather.setIcon(hour.getWeather().get(0).getIcon());
                            if (hour.getWind() != null) {
                                weather.setWind_speed(hour.getWind().getSpeed());
                            }
                            weather.setRain(hour.getRain() == null ? 0.0 : hour.getRain().getVal());
                            weather.setSnow(hour.getSnow() == null ? 0.0 : hour.getSnow().getVal());
                            forecast.add(weather);
                        }
                        callback.onWeatherLoaded(forecast);
                    }

                    @Override
                    public void onFailure(Call<Forecast> call, Throwable t) {
                        t.printStackTrace();
                        callback.onDataNotAvailable(t);
                    }
                });
    }

    @Override
    public Observable<List<OrmWeather>> getForecast(int cityId, boolean isNetworkAvailable) {
        return mService.getForecast2(ApiClient.APPID, cityId, "metric")
                .flatMap(new Func1<Forecast, Observable<List<OrmWeather>>>() {
                    @Override
                    public Observable<List<OrmWeather>> call(Forecast sub) {
                        List<OrmWeather> forecast = new ArrayList<>(sub.getList().size());
                        for (WeatherHour hour : sub.getList()) {
                            OrmWeather weather = new OrmWeather();
                            weather.setCity_id(sub.getCity().getId());
                            weather.setCity_name(sub.getCity().getName());
                            weather.setDt(new Date(hour.getDt() * 1000));
                            weather.setClouds(hour.getClouds().getAll());
                            weather.setHumidity(hour.getMain().getHumidity());
                            weather.setPressure(hour.getMain().getPressure());
                            weather.setTemp(hour.getMain().getTemp());
                            weather.setTemp_min(hour.getMain().getTempMin());
                            weather.setTemp_max(hour.getMain().getTempMax());
                            weather.setIcon(hour.getWeather().get(0).getIcon());
                            if (hour.getWind() != null) {
                                weather.setWind_speed(hour.getWind().getSpeed());
                            }
                            weather.setRain(hour.getRain() == null ? 0.0 : hour.getRain().getVal());
                            weather.setSnow(hour.getSnow() == null ? 0.0 : hour.getSnow().getVal());
                            forecast.add(weather);
                        }
                        return Observable.just(forecast);
                    }
                });
    }

    @Override
    public void getForecast(int cityId, boolean isNetworkAvailable,
                            Date date, LoadWeatherCallback callback) {

    }

    @Override
    public void getCityList(LoadCitiesCallback callback) {
        //no need;
    }

    @Override
    public Observable<List<OrmCity>> getCityList() {
        return Observable.empty();
    }

    @Override
    public void getCityWithWeather(OrmCity city, boolean isNetworkAvailable,
                                   LoadCityWithWeatherCallback callback) {
        //no need;
    }

    @Override
    public void saveCities(List<OrmCity> cities) {
        //no need;
    }

    @Override
    public void saveCity(OrmCity city) {
        //no need;
    }

    @Override
    public void refreshAllForecast(List<OrmWeather> forecast) {
        //no need;
    }

    @Override
    public void refreshForecast(int cityId, List<OrmWeather> forecast) {
        //no need;
    }

    @Override
    public void deleteAllForecast() {
        //no need;
    }

    @Override
    public void deleteForecast(int cityId) {
        //no need;
    }

    @Override
    public void saveForecast(List<OrmWeather> forecast) {
        //no need;
    }

    @Override
    public void deleteCity(OrmCity city) {
        //no need;
    }
}
