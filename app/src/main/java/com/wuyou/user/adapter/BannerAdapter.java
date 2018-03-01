package com.wuyou.user.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wuyou.user.util.GlideBannerLoader;

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
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(imageViews.get(position));
        View view = imageViews.get(position);
//        if (listener != null) {
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.OnBannerClick(toRealPosition(position));
//                }
//            });
//        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    private GlideBannerLoader imageLoader = new GlideBannerLoader();

    public void setImages(List<String> imageUrls) {
        this.urls = imageUrls;
        this.count = imageUrls.size();
    }

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
