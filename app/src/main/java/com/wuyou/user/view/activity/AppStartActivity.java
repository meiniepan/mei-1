package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wuyou.user.R;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class AppStartActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getCtx(), MainActivity.class));
                finish();
            }
        }, 500);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }
}
