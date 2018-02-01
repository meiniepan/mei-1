package com.wuyou.user.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class LoginActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_verify)
    EditText loginVerify;
    @BindView(R.id.login_send_verify)
    Button sendCaptcha;

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.login_send_verify, R.id.login, R.id.login_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_send_verify:
                String phone = loginPhone.getText().toString().trim();
//                if (!CommonUtil.checkPhone("", phone, this)) return;
                mPresenter.getVerifyCode(phone);
                break;
            case R.id.login:
//                if (!CommonUtil.checkPhone("", phone, this)) return;
                showLoadingDialog();
                mPresenter.doLogin(loginPhone.getText().toString().trim(), loginVerify.getText().toString().trim());
                break;
            case R.id.login_protocol:
                ToastUtils.ToastMessage(getCtx(), CarefreeApplication.getInstance().getUserInfo().getName());
                break;
        }
    }

    @Override
    public void loginSuccess() {
        ToastUtils.ToastMessage(getCtx(), "login success");
        Intent view = new Intent(this, MainActivity.class);
        startActivity(view);
    }

    @Override
    public void getVerifySuccess() {
        RxUtil.countdown(60).subscribe(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer value) {
                sendCaptcha.setText(value + "秒");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                sendCaptcha.setText(R.string.send_captcha);
            }
        });
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getCtx(), message);
    }
}
