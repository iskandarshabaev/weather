package com.ishabaev.weather.addcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;

import java.util.List;

/**
 * Created by ishabaev on 20.06.16.
 */
public class AddCityViewAdapter extends RecyclerView.Adapter<AddCityViewAdapter.ViewHolder> {

    public interface AddCityRecyclerViewItemListener{
        void onItemClick(OrmCity city);
    }

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
                .inflate(R.layout.add_city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(mCities.get(position));
                }
            }
        });
        holder.cityName.setText(mCities.get(position).getCity_name());
        holder.country.setText(mCities.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView cityName;
        public TextView country;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.cityName = (TextView) view.findViewById(R.id.city_name);
            this.country = (TextView) view.findViewById(R.id.country);
        }
    }
}
