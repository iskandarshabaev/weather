package com.ishabaev.weather.citydetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.dao.Weather;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class HoursRecyclerViewAdapter extends RecyclerView.Adapter<HoursRecyclerViewAdapter.ViewHolder> {


    private final List<Weather> mHours;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public HoursRecyclerViewAdapter(List<Weather> hours) {
        mHours = hours;
    }

    /*
    public void addHour(City city) {
        mHours.add(mHours.size(), city);
        notifyItemInserted(mHours.size());
    }

    public void addHours(List<City> cities) {
        mHours.addAll(mHours.size(), cities);
        notifyDataSetChanged();
    }
    */

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
       // holder.contentView.setText(mHours.get(position).getCity_name());
        //holder.city = mHours.get(position);
        //holder.mContentView.setText(mValues.get(position).content);
        holder.time.setText(format.format(mHours.get(position).getDt()));

        String temperature = mHours.get(position).getTemp() > 0 ? "+" +
                Integer.toString(mHours.get(position).getTemp().intValue()) :
                Integer.toString(mHours.get(position).getTemp().intValue());

        temperature += " °C";

        holder.temperature.setText(temperature);

        String wind = "Ветер: " + mHours.get(position).getWind_speed().toString() + " km/h";
        holder.wind.setText(wind);

        String hummidity = "Влажность: " + mHours.get(position).getHumidity().toString() + "%";
        holder.hummidity.setText(hummidity);
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
        //public final TextView temperatureView;
        public City city;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.time = (TextView) view.findViewById(R.id.date);
            this.temperature = (TextView) view.findViewById(R.id.temperature);
            this.wind = (TextView) view.findViewById(R.id.wind);
            this.hummidity = (TextView) view.findViewById(R.id.hummidity);
            //this.contentView = (TextView) view.findViewById(R.id.city_name);
            //this.temperatureView = (TextView) view.findViewById(R.id.temperature);
        }

        @Override
        public String toString() {
            return super.toString();// + " '" + contentView.getText() + "'";
        }
    }
}
