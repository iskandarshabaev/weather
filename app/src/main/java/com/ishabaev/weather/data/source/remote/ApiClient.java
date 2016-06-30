package com.ishabaev.weather.data.source.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ishabaev on 17.06.16.
 */
public class ApiClient {

    public static final String APPID = "6bbd25b118c08120fa006fbab6f7e97b";
    private static Retrofit sRetrofit;

    private ApiClient(){

    }

    public static Retrofit retrofit(){
        if(sRetrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            sRetrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl("http://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return sRetrofit;
    }
    /*
    public Call<WeatherResponse> getWeather(String cityName){
        Call<WeatherResponse> call = service.getWeather(APPID, cityName,"metric");
        return call;
    }
    */
}
