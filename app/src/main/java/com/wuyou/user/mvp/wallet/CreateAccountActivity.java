package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.util.CounterDisposableObserver;
import com.wuyou.user.util.EncryptUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/10.
 */

public class CreateAccountActivity extends BaseActivity<WalletContract.View, WalletContract.Presenter> implements WalletContract.View {
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

    private CounterDisposableObserver observer;

    @OnClick({R.id.btn_random, R.id.btn_obtain_captcha, R.id.btn_create_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_random:
                etAccountName.setText(EncryptUtil.getRandomString(12));
                break;
            case R.id.btn_obtain_captcha:
                observer = new CounterDisposableObserver(btnObtainCaptcha);
                RxUtil.countdown(Constant.COUNT_DOWN).subscribe(observer);
                mPresenter.getCaptcha(Constant.CAPTCHA_NEW_ACCOUNT, tvPhoneNum.getText().toString().trim());
                etInputCaptcha.requestFocus();
                break;
            case R.id.btn_create_1:
                if (etAccountName.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "请输入账户名称");
                    return;
                }
                String regex = "[a-z]([a-z]|[1-5]){11}";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(etAccountName.getText().toString());
                if (!m.matches()) {
                    ToastUtils.ToastMessage(getCtx(), "账户名称格式不正确！");
                    return;
                }
                if (etInputCaptcha.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "请输入验证码");
                    return;
                }
                showLoadingDialog();
//                mPresenter.checkCaptcha(Constant.CAPTCHA_CHECK_NEW_ACCOUNT, tvPhoneNum.getText().toString(), etInputCaptcha.getText().toString().trim());
                mPresenter.createAccount(etAccountName.getText().toString(), tvPhoneNum.getText().toString());
                break;
        }
    }


    @Override
    protected WalletContract.Presenter getPresenter() {
        return new WalletPresenter();
    }

    @Override
    public void createAccountSuccess() {
        Intent intent = new Intent(getCtx(), CreateAccountSuccessActivity.class);
        startActivity(intent);
        finish();
        AppManager.getAppManager().finishActivity(CreateOrImportAccountActivity.class);
    }

    @Override
    public void checkCaptchaSuccess() {
        mPresenter.createAccount(etAccountName.getText().toString(), tvPhoneNum.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (observer != null) observer.dispose();
    }

    @Override
    public void showError(String message, int res) {
        if (res == 500) {
            ToastUtils.ToastMessage(getCtx(), "创建失败，账号或手机号有误");
            return;
        } else if (res == Constant.GET_CAPTCHA_FAIL) {
            observer.onComplete();
        }
        ToastUtils.ToastMessage(getCtx(), message);
    }
}
