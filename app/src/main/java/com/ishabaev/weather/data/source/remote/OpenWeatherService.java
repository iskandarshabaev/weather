package com.ishabaev.weather.data.source.remote;

import com.ishabaev.weather.data.model.Forecast;
import com.ishabaev.weather.data.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("APPID") String appid, @Query("q") String q, @Query("units") String units);


    @GET("data/2.5/forecast")
    Call<Forecast> getForecast(@Query("APPID") String appid, @Query("id") int id, @Query("units") String units);

    @GET("data/2.5/forecast")
    Observable<Forecast> getForecast2(@Query("APPID") String appid, @Query("id") int id, @Query("units") String units);
}
