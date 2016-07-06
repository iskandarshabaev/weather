package com.ishabaev.weather.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 17.06.16.
 */
public class Main {

    @SerializedName("temp")
    private Double mTemp;

    @SerializedName("pressure")
    private Double mPressure;

    @SerializedName("humidity")
    private Double mHumidity;

    @SerializedName("temp_min")
    private Double mTempMin;

    @SerializedName("temp_max")
    private Double mTempMax;

    public Double getTemp() {
        return mTemp;
    }

    public void setTemp(Double temp) {
        this.mTemp = temp;
    }

    public Double getPressure() {
        return mPressure;
    }

    public void setPressure(Double pressure) {
        this.mPressure = pressure;
    }

    public Double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Double humidity) {
        this.mHumidity = humidity;
    }

    public Double getTempMin() {
        return mTempMin;
    }

    public void setTempMin(Double tempMin) {
        this.mTempMin = tempMin;
    }

    public Double getTempMax() {
        return mTempMax;
    }

    public void setTempMax(Double tempMax) {
        this.mTempMax = tempMax;
    }
}
