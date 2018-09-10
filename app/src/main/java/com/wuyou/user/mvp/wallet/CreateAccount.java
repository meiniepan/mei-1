package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.util.EncryptUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/10.
 */

public class CreateAccount extends BaseActivity<WalletContract.View, WalletContract.Presenter> implements WalletContract.View {
    @BindView(R.id.et_account_name)
    EditText etAccountName;
    @BindView(R.id.btn_random)
    Button btnRandom;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.et_input_captcha)
    EditText etInputCaptcha;
    @BindView(R.id.btn_obtain_captcha)
    Button btnObtainCaptcha;
    @BindView(R.id.tv_captcha_error)
    TextView tvCaptchaError;
    @BindView(R.id.btn_create_1)
    Button btnCreate1;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.create_account));
        tvPhoneNum.setText(CarefreeDaoSession.getInstance().getUserInfo().getMobile());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_account;
    }


    @OnClick({R.id.btn_random, R.id.btn_obtain_captcha, R.id.btn_create_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_random:
                etAccountName.setText(EncryptUtil.getRandomString(12));
                break;
            case R.id.btn_obtain_captcha:
                break;
            case R.id.btn_create_1:
                mPresenter.createAccount(etAccountName.getText().toString(), tvPhoneNum.getText().toString());
                break;
        }
    }

    @Override
    protected WalletContract.Presenter getPresenter() {
        return new WalletPresenter();
    }

    @Override
    public void signUpSuccess() {

    }

    @Override
    public void createAccountSuccess() {
        Intent intent = new Intent(getCtx(), CreateAccountSuccess.class);
        startActivity(intent);
    }

    @Override
    public void getWalletInfoSuccess() {

    }
}
