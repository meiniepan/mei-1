package com.wuyou.user;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.gs.buluo.common.BaseApplication;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.bean.UserInfoDao;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.MainActivity;
import com.wuyou.user.view.activity.SettingActivity;

import java.io.File;
import java.util.List;

/**
 * Created by hjn on 2016/11/1.
 */
public class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        CrashReport.initCrashReport(getApplicationContext(), "079415cb31", false);
        initBuglyUpgrade();
    }

    private void initBuglyUpgrade() {
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(LoginActivity.class);
        Beta.canShowUpgradeActs.add(SettingActivity.class);
        Bugly.init(getApplicationContext(), "ba97fc54df", false);
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
