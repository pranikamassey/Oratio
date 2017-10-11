package com.darklightning.partycatrers.Authentication.Details;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.darklightning.partycatrers.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rikki on 10/11/17.
 */

public class PhotosPagerAdapter extends PagerAdapter
{
    Activity activity;
    ArrayList<String> other_images;
    CollapsingToolbarLayout collapsingToolbarLayout;
    public PhotosPagerAdapter(Activity activity, ArrayList<String> other_images, CollapsingToolbarLayout collapsingToolbarLayout) {
        this.activity = activity;
        this.other_images = other_images;
        this.collapsingToolbarLayout = collapsingToolbarLayout;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.viewpager_image, container, false);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.view_pager_image);
        Log.e("show_images"," "+other_images.get(position));
        Picasso.with(activity).load(other_images.get(position)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if(position==0) {
                    double viewWidth = displayMetrics.widthPixels;
                    Log.e("mah_width",""+viewWidth);
                    collapsingToolbarLayout.getLayoutParams().height = (int)viewWidth;
                }
            }

            @Override
            public void onError() {

            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return other_images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView((View) object);
    }
}
