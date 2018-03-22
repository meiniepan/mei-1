package com.wuyou.user.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gnway.bangwoba.view.DensityUtils;
import com.wuyou.user.R;

/**
 * Created by hjn on 2016/11/1.
 */
public class GlideUtils {
    public static void loadImage(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.mipmap.default_pic)).into(imageView);
    }

    public static void loadImageNoHolder(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, boolean isCircle) {
        if (url == null) return;
        if (isCircle) {
            Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.mipmap.default_pic).apply(RequestOptions.circleCropTransform())).into(imageView);
        } else {
            loadImage(context, url, imageView);
        }
    }


    public interface OnLoadListener {
        void onLoaded();
    }

    public static void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        if (url == null) return;
        Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.mipmap.default_pic).override(width, height)).into(imageView);
    }


    public static void loadRoundCornerImage(Context context, String url, ImageView imageView, int dp) {
        if (url == null) return;
        RequestOptions options = new RequestOptions();
        options.optionalTransform(new GlideRoundTransform(context, dp, GlideRoundTransform.CornerType.ALL));
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView imageView) {
        if (url == null) return;
        RequestOptions options = new RequestOptions();
        options.optionalTransform(new GlideRoundTransform(context, DensityUtils.dp2px(context, 4), GlideRoundTransform.CornerType.ALL));
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
