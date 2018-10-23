package com.wuyou.user.mvp.kyc;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/19.
 */

public class KycAuthActivity extends BaseActivity {
    @BindView(R.id.tv_kyc_name)
    TextView tvKycName;
    @BindView(R.id.et_kyc_id)
    EditText etKycId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_kyc_auth;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("KYC认证");
    }

    @OnClick(R.id.btn_kyc_cinfirm)
    public void onViewClicked() {
    }
}
