package com.ishabaev.weather.cities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.citydetail.CityDetailActivity;
import com.ishabaev.weather.citydetail.CityDetailFragment;
import com.ishabaev.weather.dao.City;
import com.ishabaev.weather.data.CityWithWeather;

import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {

    public interface CitiesRecyclerViewItemListener{
        void onItemClick(CityWithWeather city);
    }

    private final List<CityWithWeather> mCities;
    private CitiesRecyclerViewItemListener mListener;

    public CitiesRecyclerViewAdapter(List<CityWithWeather> cities) {
        mCities = cities;
    }

    public void setListener(CitiesRecyclerViewItemListener listener) {
        this.mListener = listener;
    }

    public void addCity(CityWithWeather city) {
        mCities.add(mCities.size(), city);
        notifyItemInserted(mCities.size());
    }

    public void addCities(List<CityWithWeather> cities) {
        mCities.addAll(mCities.size(), cities);
        notifyDataSetChanged();
    }

    public void clear() {
        mCities.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.city = mCities.get(position);
        holder.contentView.setText(holder.city.getCity().getCity_name());
        holder.temperatureView.setText("Температура: " + holder.city.getWeather().getTemp().toString() + "  °C");
        holder.windView.setText("Ветер: " + holder.city.getWeather().getWind_speed().toString() + " km\\h");
        holder.hummidity.setText("Влажность: " + holder.city.getWeather().getHumidity().toString() + "%");

        //holder.mContentView.setText(mValues.get(position).content);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(holder.city);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView contentView;
        public TextView temperatureView;
        public TextView windView;
        public TextView hummidity;
        public CityWithWeather city;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.contentView = (TextView) view.findViewById(R.id.city_name);
            this.temperatureView = (TextView) view.findViewById(R.id.temperature);
            this.windView = (TextView) view.findViewById(R.id.wind);
            this.hummidity = (TextView) view.findViewById(R.id.hummidity);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
