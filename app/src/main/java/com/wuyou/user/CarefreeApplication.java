package com.wuyou.user;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tendcloud.tenddata.TCAgent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.MainActivity;
import com.wuyou.user.view.activity.SettingActivity;

/**
 * Created by hjn on 2016/11/1.
 */
public class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;
    public long lastSignTime;
    private String webUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUrl();
        initBuglyUpgrade();
        initTalkingData();
    }
    private void initTalkingData() {
        TCAgent.LOG_ON=true;
        TCAgent.init(this, "674BE02AC6D6485DB2C4CB05C47C7AFB", "android");
        TCAgent.setReportUncaughtExceptions(true);
    }

    private void initUrl() {
        String baseUrl = SharePreferenceManager.getInstance(this).getStringValue(Constant.SP_BASE_URL);
        if (!TextUtils.isEmpty(baseUrl)) Constant.BASE_URL = baseUrl;
        webUrl = SharePreferenceManager.getInstance(this).getStringValue(Constant.SP_WEB_URL);
        if (!TextUtils.isEmpty(webUrl)) Constant.WEB_URL = webUrl;
    }

    private void initBuglyUpgrade() {
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(LoginActivity.class);
        Beta.canShowUpgradeActs.add(SettingActivity.class);
        Beta.autoDownloadOnWifi = true;
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
