package com.wuyou.user.mvp.wallet;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by DELL on 2018/9/10.
 */

public class BackupPKeyActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.backup_pk));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_backup_pk;
    }

}
