package com.wuyou.user;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import junit.framework.TestResult;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

    }
    public static int dp2px(Context context, float dpVal) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dpVal * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dip2pix(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public TestResult run() {
        int i = dp2px(getContext(), 2);
        int j = dip2pix(getContext(), 2);
        return super.run();
    }
}