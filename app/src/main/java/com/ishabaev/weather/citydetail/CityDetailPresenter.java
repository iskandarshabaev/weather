package com.ishabaev.weather.citydetail;

import android.os.AsyncTask;
import android.provider.Settings;

import com.ishabaev.weather.data.Main;
import com.ishabaev.weather.data.WeatherResponse;
import com.ishabaev.weather.rest.ApiClient;
import com.ishabaev.weather.rest.OpenWeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishabaev on 18.06.16.
 */
public class CityDetailPresenter implements CityDetailContract.UserActionsListener{

    private OpenWeatherService mService = ApiClient.retrofit().create(OpenWeatherService.class);
    private CityDetailContract.View mCityDetailView;

    public CityDetailPresenter(CityDetailContract.View cityDetailView){
        mCityDetailView = cityDetailView;
    }

    @Override
    public void openCity(String cityName) {
        /*
        mCityDetailView.showProgressBar(true);
        mService.getWeather(ApiClient.APPID, cityName, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();
                        Main main = weatherResponse.getMain();
                        main.getTemp();
                        mCityDetailView.showProgressBar(false);
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        mCityDetailView.showProgressBar(false);
                    }
                });
                */
        ProgressBarTestTask progressBarTestTask = new ProgressBarTestTask();
        progressBarTestTask.execute();
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
