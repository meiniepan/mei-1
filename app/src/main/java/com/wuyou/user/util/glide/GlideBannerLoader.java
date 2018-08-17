package com.wuyou.user.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hjn on 2016/11/2.
 */
public class GlideBannerLoader extends ImageLoader {
    private boolean isLocal = false;
    private boolean isRound = false;

    public GlideBannerLoader(boolean isRound) {
        this.isRound = isRound;
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
        if (isRound) {
            RequestOptions options = new RequestOptions().optionalTransform(new GlideRoundTransform(context, 4, GlideRoundTransform.CornerType.ALL));
            Glide.with(context).load(url).apply(options).into(imageView);
        } else {
            Glide.with(context).load(url).into(imageView);
        }
    }
}
