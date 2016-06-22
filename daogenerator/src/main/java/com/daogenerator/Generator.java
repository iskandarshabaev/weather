package com.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {

    public static void main(String[] args){
        Schema schema = new Schema(8, "com.ishabaev.weather.dao");
        initCityTable(schema);
        initWeatherTable(schema);

        try {
            new DaoGenerator().generateAll(schema, "app/src/main/java/");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void initCityTable(Schema schema){
        Entity note= schema.addEntity("City");
        note.addLongProperty("_id").primaryKey();
        note.addStringProperty("city_name");
        note.addStringProperty("country");
        note.addDoubleProperty("lat");
        note.addDoubleProperty("lon");
    }

    public static void initWeatherTable(Schema schema){
        Entity note= schema.addEntity("Weather");
        note.addIdProperty();
        note.addLongProperty("city_id");
        note.addStringProperty("city_name");
        note.addDateProperty("dt");
        note.addDoubleProperty("temp");
        note.addDoubleProperty("temp_min");
        note.addDoubleProperty("temp_max");
        note.addDoubleProperty("pressure");
        note.addDoubleProperty("humidity");
        note.addDoubleProperty("clouds");
        note.addDoubleProperty("wind_speed");
        note.addDoubleProperty("wind_deg");
        note.addDoubleProperty("rain");
        note.addDoubleProperty("snow");

    }
}
