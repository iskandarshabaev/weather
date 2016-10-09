package com.ishabaev.weather.addcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;

import java.util.List;
import java.util.Random;

public class AddCityViewAdapter extends RecyclerView.Adapter<AddCityViewAdapter.ViewHolder> {

    private Random mRandom = new Random();
    private final static int MIN = 0;
    private final static int MAX = 7;
    private final List<OrmCity> mCities;
    private AddCityRecyclerViewItemListener mListener;

    public AddCityViewAdapter(List<OrmCity> cities) {
        mCities = cities;
    }

    public void setListener(AddCityRecyclerViewItemListener listener) {
        this.mListener = listener;
    }

    public void addCity(OrmCity city) {
        mCities.add(mCities.size(), city);
        notifyItemInserted(mCities.size());
    }

    public void addCities(List<OrmCity> cities) {
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
                .inflate(R.layout.item_add_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.view.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(mCities.get(holder.getAdapterPosition()));
            }
        });
        holder.cityName.setText(mCities.get(position).getCity_name());
        holder.country.setText(mCities.get(position).getCountry());
        holder.imageView.setImageResource(getRandomCityIcon());
    }

    private int getRandomCityIcon() {
        int r = mRandom.nextInt(MAX - MIN + 1) + MIN;
        switch (r) {
            case 0:
                return R.drawable.city0;
            case 1:
                return R.drawable.city1;
            case 2:
                return R.drawable.city2;
            case 3:
                return R.drawable.city3;
            case 4:
                return R.drawable.city4;
            case 5:
                return R.drawable.city5;
            case 6:
                return R.drawable.city6;
            default:
                return R.drawable.city7;
        }
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView cityName;
        public TextView country;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.cityName = (TextView) view.findViewById(R.id.city_name);
            this.country = (TextView) view.findViewById(R.id.country);
            this.imageView = (ImageView) view.findViewById(R.id.ic_city);
        }
    }

    public interface AddCityRecyclerViewItemListener {
        void onItemClick(OrmCity city);
    }
}
