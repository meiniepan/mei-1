package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/31.
 */

public class ApplySuccessActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_apply_success;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.apply_success));
    }


    @OnClick({R.id.tv_check_record, R.id.tv_return_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_record:
                startActivity(new Intent(getCtx(), TBVolunteerRecordActivity.class));
                break;
            case R.id.tv_return_home:
                startActivity(new Intent(getCtx(), TimeBankMainActivity.class));
                break;
        }
        finish();
    }
}
