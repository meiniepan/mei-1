package com.wuyou.user;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.gs.buluo.common.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by hjn on 2016/11/1.
 */
public class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashReport.initCrashReport(getApplicationContext(), "079415cb31", false);
    }

    public static synchronized CarefreeApplication getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public String getFilePath() {
        return getFilesDir().getAbsolutePath();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
