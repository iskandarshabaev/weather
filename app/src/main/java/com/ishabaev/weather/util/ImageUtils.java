package com.ishabaev.weather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by ishabaev on 25.06.16.
 */
public class ImageUtils {

    private Context mContext;

    public ImageUtils(Context context){
        mContext = context;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromAssets(String assetName, int reqWidth, int reqHeight) {
        try {
            InputStream ims = mContext.getAssets().open(assetName);


//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeStream(ims, null, options);
//
//            options.inJustDecodeBounds = false;
//            // recreate the stream
//            // make some calculation to define inSampleSize
//            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//            bitmap = BitmapFactory.decodeStream(ims, null, options);
//            return bitmap;

            Bitmap icon = BitmapFactory.decodeStream(ims);
            Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, reqWidth, reqHeight, false);
            return icon;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
