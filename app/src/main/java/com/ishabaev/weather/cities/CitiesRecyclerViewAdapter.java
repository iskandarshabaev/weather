package com.ishabaev.weather.cities;

import android.content.Context;
import android.content.res.Resources;
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

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ishabaev on 19.06.16.
 */
public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {

    public interface CitiesRecyclerViewItemListener {

        void onItemClick(CityWithWeather city);
    }

    private Context mContext;
    private List<CityWithWeather> mCities;
    private CitiesRecyclerViewItemListener mListener;
    private ImageUtils mImageUtils;
    private int mCurrentPosition = -1;

    public CitiesRecyclerViewAdapter(Context context, List<CityWithWeather> cities) {
        mCities = cities;
        mContext = context;
    }

    public void setListener(CitiesRecyclerViewItemListener listener) {
        this.mListener = listener;
    }

    public void addCity(CityWithWeather city) {
        if (!haveCityYet(city)) {
            mCities.add(mCities.size(), city);
            DataSort.sortCityWithWeatherList(mCities);
            notifyDataSetChanged();
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int value) {
        mCurrentPosition = value;
        notifyDataSetChanged();
    }

    private boolean haveCityYet(CityWithWeather cityWithWeather) {
        for (CityWithWeather city : mCities) {
            if (city.getCity().get_id().equals(cityWithWeather.getCity().get_id())) {
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

    public void removeCity(int location) {
        mCities.remove(location);
        notifyItemRemoved(location);
    }

    public OrmCity getCity(int location) {
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
        if (holder.city.getWeather() != null) {

            Resources res = holder.view.getResources();
            String temperature = res.getString(R.string.temperature) + ": ";
            temperature += holder.city.getWeather().getTemp() == null ? "-/-" :
                    holder.city.getWeather().getTemp().toString() + " °C";
            holder.temperatureView.setText(temperature);

            if (holder.windView != null) {
                String wind = holder.city.getWeather().getWind_speed() == null ?
                        res.getString(R.string.windless) :
                        res.getString(R.string.wind) + ": " +
                                holder.city.getWeather().getWind_speed().toString() + " " +
                                res.getString(R.string.km_h);
                holder.windView.setText(wind);
            }

            if (holder.hummidity != null) {
                String hummidity = res.getString(R.string.humidity) + ": ";
                hummidity += holder.city.getWeather().getHumidity() == null ? "-/-" :
                        holder.city.getWeather().getHumidity().toString() + "%";
                holder.hummidity.setText(hummidity);
            }

            float scaleRatio = holder.view.getResources().getDisplayMetrics().density;
            float dimenPix = holder.view.getResources().getDimension(R.dimen.city_image_size);
            int size = (int) (dimenPix / scaleRatio);
            holder.imageView.setImageDrawable(null);
            if (holder.city.getWeather().getIcon() != null) {
                loadBitmap(holder.city.getWeather().getIcon(), holder.imageView, size);
            }
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(mCurrentPosition);
                mCurrentPosition = holder.getAdapterPosition();
                notifyItemChanged(mCurrentPosition);
                if (mListener != null) {
                    mListener.onItemClick(holder.city);
                }
            }
        });
        holder.view.setSelected(mCurrentPosition == position);
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

    public void loadBitmap(String imageName, ImageView imageView, int size) {
        if (cancelPotentialWork(imageName, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, size, size);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imageName);
        }
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.imageName;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
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

        BitmapWorkerTask(ImageView imageView, int width, int height) {
            mWidth = width;
            mHeight = height;
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return mImageUtils.decodeSampledBitmapFromAssets(params[0] + ".jpg", mWidth / 2, mHeight / 2);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
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
