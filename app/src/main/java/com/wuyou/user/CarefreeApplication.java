package com.wuyou.user;

import android.os.Environment;

import com.gs.buluo.common.BaseApplication;
import com.wuyou.user.bean.UserInfo;

/**
 * Created by hjn on 2016/11/1.
 */
public abstract class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;
    private UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized CarefreeApplication getInstance() {
        return instance;
    }


    @Override
    public String getFilePath() {
        return Environment.getExternalStorageDirectory().toString() + "/carefree/";
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
