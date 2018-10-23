package com.wuyou.user.mvp.kyc;

import android.os.Bundle;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/19.
 */

public class KycAuthFinishActivity extends BaseActivity {

    @BindView(R.id.tv_kyc_finish_name)
    TextView tvKycFinishName;
    @BindView(R.id.et_kyc_finish_id)
    TextView etKycFinishId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_kyc_auth_finish;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("KYC认证");
    }

}
