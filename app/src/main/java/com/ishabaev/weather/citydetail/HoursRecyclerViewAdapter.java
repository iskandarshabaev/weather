package com.ishabaev.weather.citydetail;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.dao.OrmWeather;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class HoursRecyclerViewAdapter extends RecyclerView.Adapter<HoursRecyclerViewAdapter.ViewHolder> {


    private final List<OrmWeather> mHours;
    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

    public HoursRecyclerViewAdapter(List<OrmWeather> hours) {
        mHours = hours;
    }

    public void addElement(OrmWeather Weather) {
        mHours.add(mHours.size(), Weather);
        notifyItemInserted(mHours.size());
    }

    public void addElements(List<OrmWeather> weatherList) {
        mHours.addAll(mHours.size(), weatherList);
        notifyDataSetChanged();
    }

    public void clear() {
        mHours.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Resources res = holder.view.getResources();
        holder.time.setText(mFormat.format(mHours.get(position).getDt()));
        String temperature = mHours.get(position).getTemp() > 0 ? "+" +
                Integer.toString(mHours.get(position).getTemp().intValue()) :
                Integer.toString(mHours.get(position).getTemp().intValue());
        temperature += " °C";
        holder.temperature.setText(temperature);

        String wind = res.getString(R.string.wind) + ": ";
        wind += mHours.get(position).getWind_speed() == null ? "" :
                mHours.get(position).getWind_speed().toString() + " " +
                        res.getString(R.string.km_h);
        holder.wind.setText(wind);

        String hummidity = res.getString(R.string.humidity) + ": ";
        hummidity += mHours.get(position).getHumidity() == null ? "" :
                mHours.get(position).getHumidity().toString() + "%";
        holder.hummidity.setText(hummidity);

        String pressure = res.getString(R.string.pressure) + ": ";
        pressure += mHours.get(position).getPressure() == null ? "" :
                mHours.get(position).getPressure().toString();
        holder.pressure.setText(pressure);

        Drawable img = ContextCompat.getDrawable(holder.view.getContext(),
                getIcon(mHours.get(position).getIcon()));
        holder.weatherState.setImageDrawable(img);
    }

    private int getIcon(String iconName){
        switch (iconName){
            case "01d":
                return R.drawable.d01;
            case "01n":
                return R.drawable.n01;
            case "02d":
                return R.drawable.d02;
            case "02n":
                return R.drawable.n02;
            case "03d":
                return R.drawable.dn03;
            case "03n":
                return R.drawable.dn03;
            case "04d":
                return R.drawable.dn04;
            case "04n":
                return R.drawable.dn04;
            case "09d":
                return R.drawable.dn09;
            case "09n":
                return R.drawable.dn09;
            case "10d":
                return R.drawable.dn10;
            case "10n":
                return R.drawable.dn10;
            case "11d":
                return R.drawable.dn11;
            case "11n":
                return R.drawable.dn11;
            case "13d":
                return R.drawable.dn13;
            case "13n":
                return R.drawable.dn13;
            default:
                return R.drawable.n01;
        }
    }

    @Override
    public int getItemCount() {
        return mHours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView time;
        public TextView temperature;
        public TextView wind;
        public TextView hummidity;
        public TextView pressure;
        public ImageView weatherState;
        public OrmCity city;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.time = (TextView) view.findViewById(R.id.date);
            this.temperature = (TextView) view.findViewById(R.id.temperature);
            this.wind = (TextView) view.findViewById(R.id.wind);
            this.hummidity = (TextView) view.findViewById(R.id.hummidity);
            this.pressure = (TextView) view.findViewById(R.id.pressure);
            this.weatherState = (ImageView) view.findViewById(R.id.weather_state);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}