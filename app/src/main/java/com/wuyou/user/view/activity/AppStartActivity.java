package com.wuyou.user.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.BrowserData;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class AppStartActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        disableFitSystemWindow();
        setBarColor(R.color.transparent);

        String uriData = getIntent().getDataString();
        if (TextUtils.isEmpty(uriData)) {
            new Handler().postDelayed(this::jump, 1200);
        } else {
            Uri uri = Uri.parse(uriData);
            String path = uri.getPath();
            try {
                String gson = uri.getQueryParameter("params");
                byte[] decode = Base64.decode(gson, Base64.NO_WRAP);
                BrowserData data = new Gson().fromJson(new String(decode), BrowserData.class);
                if (data != null) {
                    Intent intent = new Intent(getCtx(), MainActivity.class);
                    intent.putExtra(Constant.ACTIVITY_URL, data.url);
                    startActivity(intent);
                    finish();
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    private void jump() {
        if (SharePreferenceManager.getInstance(getApplicationContext()).getBooeanValue(Constant.FIRST_START + getVersionCode())) {
            SharePreferenceManager.getInstance(getApplicationContext()).setValue(Constant.FIRST_START + getVersionCode(), false);
            startActivity(new Intent(getCtx(), GuideActivity.class));
            finish();
        } else {
            startActivity(new Intent(getCtx(), MainActivity.class));
            finish();
        }
    }

    public int getVersionCode() {
        PackageManager manager;

        PackageInfo info = null;

        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }
}
