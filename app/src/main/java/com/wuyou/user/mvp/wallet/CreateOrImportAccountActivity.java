package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.common.utils.SharePreferenceManager;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/10.
 */

public class CreateOrImportAccountActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.create_or_import));
        if (SharePreferenceManager.getInstance(getCtx()).getBooeanValue(Constant.CREATE_ACCOUNT_FLAG,false)) {
            findViewById(R.id.btn_create).setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_or_import_account;
    }

    @OnClick({R.id.btn_import, R.id.btn_create})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_import:
                intent = new Intent(getCtx(), ImportAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_create:
                intent = new Intent(getCtx(), CreateAccountActivity.class);
                startActivity(intent);
                break;
        }
    }
}
