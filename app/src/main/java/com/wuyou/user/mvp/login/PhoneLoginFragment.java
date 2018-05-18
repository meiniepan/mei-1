package com.wuyou.user.mvp.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.R;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.CounterDisposableObserver;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by solang on 2018/2/2.
 */

public class PhoneLoginFragment extends BaseFragment<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_verify)
    EditText loginVerify;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.login_send_verify)
    Button reSendCaptcha;
    private CounterDisposableObserver observer;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_login_phone;
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getContext(), message);
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
    }

    @Override
    public void loginSuccess() {
        EventBus.getDefault().post(new LoginEvent());
        ToastUtils.ToastMessage(getContext(), "登录成功");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void getVerifySuccess() {

    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.login_send_verify, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_send_verify:
                String phone = loginPhone.getText().toString().trim();
                if (!CommonUtil.checkPhone("", phone, mCtx)) return;
                mPresenter.getVerifyCode(phone);
                loginVerify.requestFocus();
                observer = new CounterDisposableObserver(reSendCaptcha);
                RxUtil.countdown(59).subscribe(observer);
                break;
            case R.id.login:
                String phone2 = loginPhone.getText().toString().trim();
                if (!CommonUtil.checkPhone("", phone2, mCtx)) return;
                showLoadingDialog();
                mPresenter.doLogin(phone2, loginVerify.getText().toString().trim());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (observer != null) {
            observer.dispose();
        }

    }
}
