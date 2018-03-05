package com.wuyou.user.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hjn on 2016/11/2.
 */
public class GlideBannerLoader extends ImageLoader {
    private boolean isLocal = false;

    public GlideBannerLoader(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public GlideBannerLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (isLocal) {
            imageView.setBackgroundResource((int) path);
            return;
        }
        if (path == null) return;
        String url = path.toString();
//        if (!url.contains("://")) {
//            url = Constant.Base.BASE_IMG_URL + url;
//        } else {
//            url = transformUrl(url);
//        }
        Glide.with(context).load(url).into(imageView);
    }
}
