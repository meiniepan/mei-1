package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by DELL on 2018/10/26.
 */

public class TBVolunteerRecordActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_bank_volunteer_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.project_record);
    }
}
