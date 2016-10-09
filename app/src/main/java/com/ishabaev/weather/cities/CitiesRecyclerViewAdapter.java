package com.ishabaev.weather.cities;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.model.CityWithWeather;
import com.ishabaev.weather.util.ImageHelper;

import java.util.List;

public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {

    public interface CitiesRecyclerViewItemListener {
        void onItemClick(@NonNull CityWithWeather city, @NonNull View view);
    }

    private List<CityWithWeather> mCities;
    private CitiesRecyclerViewItemListener mListener;
    private int mCurrentPosition = -1;

    public CitiesRecyclerViewAdapter(@NonNull List<CityWithWeather> cities) {
        mCities = cities;
    }

    public void setListener(@NonNull CitiesRecyclerViewItemListener listener) {
        this.mListener = listener;
    }

    public void addCity(@NonNull CityWithWeather city) {
        if (!haveCityYet(city)) {
            mCities.add(mCities.size(), city);
            notifyItemChanged(mCities.size());
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int value) {
        mCurrentPosition = value;
        notifyDataSetChanged();
    }

    private boolean haveCityYet(@NonNull CityWithWeather cityWithWeather) {
        for (CityWithWeather city : mCities) {
            if (city.getCity().get_id().equals(cityWithWeather.getCity().get_id())) {
                return true;
            }
        }
        return false;
    }

    public void setCities(@NonNull List<CityWithWeather> cities) {
        mCities.clear();
        mCities.addAll(mCities.size(), cities);
        notifyDataSetChanged();
    }

    public void addCities(@NonNull List<CityWithWeather> cities) {
        mCities.addAll(mCities.size(), cities);
        notifyDataSetChanged();
    }

    public void removeCity(int location) {
        mCities.remove(location);
        notifyItemRemoved(location);
    }

    @NonNull
    public OrmCity getCity(int location) {
        return mCities.get(location).getCity();
    }

    public void clear() {
        mCities.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city = mCities.get(position);
        holder.contentView.setText(holder.city.getCity().getCity_name());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageView.setTransitionName(holder.contentView.getClass().getName() + position);
        }

        if (holder.city.getWeather() != null) {
            Resources res = holder.view.getResources();

            String temperatureText = holder.city.getWeather().getTemp() > 0 ?
                    res.getString(R.string.temperature_plus, (int)holder.city.getWeather().getTemp()) :
                    res.getString(R.string.temperature_minus, (int)holder.city.getWeather().getTemp());
            holder.temperatureView.setText(temperatureText);

            if (holder.windView != null) {
                String windText = res.getString(R.string.wind,
                        holder.city.getWeather().getWind_speed());
                holder.windView.setText(windText);
            }

            if (holder.humidity != null) {
                String holderText = res.getString(R.string.humidity,
                        holder.city.getWeather().getHumidity());
                holder.humidity.setText(holderText);
            }

            holder.imageView.setImageDrawable(null);
            if (holder.city.getWeather().getIcon() != null) {
                String fileName = holder.city.getWeather().getIcon() + ".jpg";
                ImageHelper.load("file:///android_asset/" + fileName, holder.imageView);
            }
        }
        holder.view.setOnClickListener(v -> {
            notifyItemChanged(mCurrentPosition);
            mCurrentPosition = holder.getAdapterPosition();
            notifyItemChanged(mCurrentPosition);
            if (mListener != null) {
                mListener.onItemClick(holder.city, holder.imageView);
            }
        });
        holder.view.setSelected(mCurrentPosition == position);
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
        public TextView humidity;
        public ImageView imageView;
        public CityWithWeather city;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.contentView = (TextView) view.findViewById(R.id.city_name);
            this.temperatureView = (TextView) view.findViewById(R.id.temperature);
            this.windView = (TextView) view.findViewById(R.id.wind);
            this.humidity = (TextView) view.findViewById(R.id.humidity);
            this.imageView = (ImageView) view.findViewById(R.id.state_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
