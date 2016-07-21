package com.ishabaev.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.data.source.local.LocalDataSource;
import com.ishabaev.weather.data.source.remote.ApiClient;
import com.ishabaev.weather.data.source.remote.OpenWeatherService;
import com.ishabaev.weather.data.source.remote.RemoteDataSource;

/**
 * Created by ishabaev on 25.06.16.
 */
public class Injection {
    public static Repository provideTasksRepository(@NonNull Context context) {
        OpenWeatherService openWeatherService = ApiClient.retrofit().create(OpenWeatherService.class);
        return Repository.getInstance(LocalDataSource.getInstance(context),
                RemoteDataSource.getInstance(openWeatherService));
    }
}
