package com.ishabaev.weather.cities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.CityWithWeather;
import com.ishabaev.weather.util.DataSort;
import com.ishabaev.weather.util.ImageUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
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
    private ImageUtils mImageUtils;

    public CitiesRecyclerViewAdapter(List<CityWithWeather> cities) {
        mCities = cities;
    }

    public void setListener(CitiesRecyclerViewItemListener listener) {
        this.mListener = listener;
    }

    public void addCity(CityWithWeather city) {
        if(!haveCityYet(city)) {
            mCities.add(mCities.size(), city);
            DataSort.sortCityWithWeatherList(mCities);
            //notifyItemInserted(mCities.size());
            notifyDataSetChanged();
        }
    }

    private boolean haveCityYet(CityWithWeather cityWithWeather){
        for(CityWithWeather city : mCities){
            if(city.getCity().get_id().equals(cityWithWeather.getCity().get_id())){
                return true;
            }
        }
        return false;
    }

    public void setCities(List<CityWithWeather> cities) {
        mCities.clear();
        mCities.addAll(mCities.size(), cities);
        notifyDataSetChanged();
    }

    public void addCities(List<CityWithWeather> cities) {
        mCities.addAll(mCities.size(), cities);
        notifyDataSetChanged();
    }

    public void removeCity(int location){
        mCities.remove(location);
        notifyItemRemoved(location);
    }

    public OrmCity getCity(int location){
        return mCities.get(location).getCity();
    }

    public void clear() {
        mCities.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_content, parent, false);
        mImageUtils = new ImageUtils(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.city = mCities.get(position);
        holder.contentView.setText(holder.city.getCity().getCity_name());
        if(holder.city.getWeather() != null) {

            String temperature = holder.city.getWeather().getTemp() == null ? "" :
                    "Температура: " + holder.city.getWeather().getTemp().toString() + " °C";
            holder.temperatureView.setText(temperature);

            String wind = holder.city.getWeather().getWind_speed() == null ? "" :
                    "Ветер: " + holder.city.getWeather().getWind_speed().toString() + " km\\h";
            holder.windView.setText(wind);


            String hummidity = holder.city.getWeather().getHumidity() == null ? "" :
                    "Влажность: " + holder.city.getWeather().getHumidity().toString() + "%";
            holder.hummidity.setText(hummidity);

            /*
            Drawable image = null;
            try {
                InputStream ims = holder.view.getContext().getAssets().open(holder.city.getWeather().getIcon() +".jpg");
                image = Drawable.createFromStream(ims, null);
                if(image != null) {
                    holder.imageView.setImageDrawable(null);
                    holder.imageView.setImageDrawable(image);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            */

            int height = holder.view.getResources().getDimensionPixelSize(R.dimen.app_bar_height);
            /*
            Bitmap bitmap = mImageUtils.decodeSampledBitmapFromAssets(
                    holder.city.getWeather().getIcon() +".jpg", height, height);
            if(bitmap != null){
                holder.imageView.setImageBitmap(bitmap);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            */
            holder.imageView.setImageDrawable(null);
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.imageView,height,height);
            bitmapWorkerTask.execute(holder.city.getWeather().getIcon());
        }

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

    public void loadBitmap(String imageName, ImageView imageView) {
        if (cancelPotentialWork(imageName, imageView)) {
            //final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            //final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            //imageView.setImageDrawable(asyncDrawable);
            //task.execute(imageName);
        }
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static boolean cancelPotentialWork(String imageName, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.imageName;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || bitmapData != imageName) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(InputStream inputStream,
                             BitmapWorkerTask bitmapWorkerTask) {
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ImageView> imageViewReference;
        private int mWidth;
        private int mHeight;
        String imageName;

        BitmapWorkerTask(ImageView imageView, int width, int height){
            mWidth = width;
            mHeight = height;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return mImageUtils.decodeSampledBitmapFromAssets(params[0] +".jpg", mWidth, mHeight);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
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
        public ImageView imageView;
        public CityWithWeather city;
        public Drawable image;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.contentView = (TextView) view.findViewById(R.id.city_name);
            this.temperatureView = (TextView) view.findViewById(R.id.temperature);
            this.windView = (TextView) view.findViewById(R.id.wind);
            this.hummidity = (TextView) view.findViewById(R.id.hummidity);
            this.imageView = (ImageView) view.findViewById(R.id.state_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
