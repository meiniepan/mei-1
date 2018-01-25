package com.wuyou.android_user;

import android.app.Application;
import android.os.Environment;

/**
 * Created by hjn on 2016/11/1.
 */
public abstract class CarefreeApplication extends Application {
    private static CarefreeApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static synchronized CarefreeApplication getInstance(){
        return instance;
    }


    public String getFilePath() {
        return Environment.getExternalStorageDirectory().toString() + "/carefree/";
    }
}
