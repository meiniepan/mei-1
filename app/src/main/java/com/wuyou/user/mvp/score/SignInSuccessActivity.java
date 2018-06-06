package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.widget.TextView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by DELL on 2018/6/6.
 */

public class SignInSuccessActivity extends BaseActivity {
    @BindView(R.id.score_record_title)
    TextView scoreRecordTitle;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        scoreRecordTitle.setText("积分 + " + getIntent().getStringExtra(Constant.SIGN_POINT));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_success;
    }
}
