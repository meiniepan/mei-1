package com.wuyou.user;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.wuyou.user.data.local.db.EosAccount;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends AndroidTestCase {
    public ApplicationTest() {
        assertNotNull("haha", "haha");
    }

    public static int dp2px(Context context, float dpVal) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dpVal * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dip2pix(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @SmallTest
    public void testDao() throws Exception {
        assertNotNull("haha", "haha");
        CarefreeDaoSession.getInstance().getEosDao().insert(EosAccount.from("hjn"));
        Log.e("Carefree", "ApplicationTest: " + CarefreeDaoSession.getInstance().getEosDao().loadAll().size());
        assertEquals("haha", "haha");
    }
}