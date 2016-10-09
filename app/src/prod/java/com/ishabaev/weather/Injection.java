package com.ishabaev.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishabaev.weather.data.source.FileManager;
import com.ishabaev.weather.data.source.FileSource;
import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.data.source.local.LocalDataSource;
import com.ishabaev.weather.data.source.remote.ApiClient;
import com.ishabaev.weather.data.source.remote.OpenWeatherService;
import com.ishabaev.weather.data.source.remote.RemoteDataSource;

public class Injection {

    public static Repository provideRepository(@NonNull Context context) {
        OpenWeatherService openWeatherService = ApiClient.retrofit().create(OpenWeatherService.class);
        return Repository.getInstance(LocalDataSource.getInstance(context),
                RemoteDataSource.getInstance(openWeatherService));
    }

    public static FileSource provideFileSource(@NonNull Context context) {
        return FileManager.getInstance(context.getAssets());
    }
}
