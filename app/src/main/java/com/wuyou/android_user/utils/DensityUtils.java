package com.wuyou.android_user.utils;

import android.content.Context;

/**
 * Created by hjn on 2016/11/1.
 */
public class DensityUtils {
    /**
     * 根据手机的分辨率�? dip 的单�? 转成�? px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
