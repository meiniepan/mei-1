package com.wuyou.user.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.wuyou.user.util.glide.GlideBannerLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2016/11/1.
 */
public class BannerAdapter extends PagerAdapter {

    private int count;
    private Context context;

    public BannerAdapter(Context context, List<String> imagesUrl) {
        this.context = context;
        this.urls = imagesUrl;
        setImageList(urls);
    }

    List<String> urls;
    ArrayList<ImageView> imageViews = new ArrayList<>();

    @Override
    public int getCount() {
        return 10000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= urls.size();
        if (position < 0) {
            position = urls.size() + position;
        }
        View view = imageViews.get(position);
        ViewParent viewParent = view.getParent();
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    private GlideBannerLoader imageLoader = new GlideBannerLoader();

    private void setImageList(List<String> imagesUrl) {
        this.count = imagesUrl.size();
        if (imagesUrl.size() <= 0) {
            return;
        }
//        initImages();
        for (int i = 0; i <= count + 1; i++) {
            ImageView imageView = null;
            if (imageLoader != null) {
                imageView = imageLoader.createImageView(context);
            }
            if (imageView == null) {
                imageView = new ImageView(context);
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Object url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            if (imageLoader != null)
                imageLoader.displayImage(context, url, imageView);
//            else
//                Log.e(tag, "Please set images loader.");
        }
    }
}
