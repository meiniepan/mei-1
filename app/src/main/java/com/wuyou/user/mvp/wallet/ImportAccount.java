package com.wuyou.user.mvp.wallet;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by Solang on 2018/9/10.
 */

public class ImportAccount extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.import_account));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_import_account;
    }
}
