package com.ishabaev.weather.util;


import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageHelper {

    public static void load(@NonNull String url, ImageView imageView) {
        Picasso.with(imageView.getContext())
                .load(url)
                //.placeholder(R.drawable.game_placeholder)
                .noFade()
                .into(imageView);
    }

}
