package com.ishabaev.weather.addcity;

import android.os.Handler;
import android.text.Editable;
import android.view.View;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.util.Translate;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ishabaev on 28.06.16.
 */
public class AddCityPresenter implements AddCityContract.Presenter {

    private AddCityContract.View mView;
    private Repository mRepository;
    private Timer timer=new Timer();
    private final long DELAY = 500;
    private boolean isTaskRunning;
    private Handler mHandler;
    private final static String FILE_NAME = "city_list.txt";

    public AddCityPresenter(AddCityContract.View view, Repository repository){
        mView = view;
        mRepository = repository;
        mHandler = new Handler();
    }

    @Override
    public void onItemClick(OrmCity city) {
        mRepository.saveCity(city);
    }

    @Override
    public void textChanged(final Editable s) {
        timer.cancel();
        isTaskRunning = false;
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        isTaskRunning = true;
                        mHandler.post(new Runnable() {
                            public void run() {
                                mView.setProgressBarVisibility(View.VISIBLE);
                                mView.clearCities();
                            }
                        });
                        try {
                            InputStream is = mRepository.open(FILE_NAME);
                            Scanner scanner = new Scanner(is);
                            int lineNum = 0;
                            int count = 0;
                            while (scanner.hasNextLine()) {
                                if (!isTaskRunning) {
                                    return;
                                }
                                String line = scanner.nextLine();
                                lineNum++;
                                if (line.toLowerCase().contains(s.toString().toLowerCase()) ||
                                        line.toLowerCase().contains(Translate.ru2en(s.toString().toLowerCase()))) {
                                    String[] cityParams = line.split("\t");

                                    final OrmCity city = new OrmCity();
                                    city.set_id(Long.parseLong(cityParams[0]));
                                    city.setCity_name(cityParams[1]);
                                    city.setLat(Double.parseDouble(cityParams[2]));
                                    city.setLon(Double.parseDouble(cityParams[3]));
                                    city.setCountry(cityParams[4]);
                                    count++;
                                    mHandler.post(new Runnable() {
                                        public void run() {
                                            mView.setImageViewVisibility(View.GONE);
                                            mView.setSearchStateVisibility(View.GONE);
                                            mView.addCityToList(city);
                                        }
                                    });
                                }
                                final int d = lineNum;
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        mView.setProgressBarValue(d);
                                    }
                                });
                                if(count > 10){
                                    break;
                                }
                            }
                            mHandler.post(new Runnable() {
                                public void run() {
                                    mView.setProgressBarVisibility(View.GONE);
                                    if(mView.getCitiesSize() == 0){
                                        mView.setImageViewVisibility(View.VISIBLE);
                                        mView.setSearchStateVisibility(View.VISIBLE);
                                        String searchStateText = mView
                                                .getResources()
                                                .getString(R.string.could_not_find_a_city);
                                        mView.setSearchStateText(searchStateText);
                                    }
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                DELAY
        );
    }
}
