package com.wuyou.user.view.activity;

import android.os.Bundle;
import android.view.View;

import com.wuyou.user.R;

/**
 * Created by DELL on 2018/5/14.
 */

public class ServeWayChooseActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_choose;
    }

    public void confirmWay(View view) {
        finish();
    }
}
