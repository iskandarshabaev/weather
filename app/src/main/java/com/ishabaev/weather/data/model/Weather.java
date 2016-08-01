package com.ishabaev.weather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ishabaev on 17.06.16.
 */
public class Weather {

    @SerializedName("id")
    private Integer mId;

    @SerializedName("main")
    private String mMain;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("icon")
    private String mIcon;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String main) {
        this.mMain = main;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

}
