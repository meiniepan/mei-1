package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.util.CounterDisposableObserver;
import com.wuyou.user.util.RxUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Created by DELL on 2018/5/8.
 */

public class ModifyPhoneActivity extends BaseActivity {
    @BindView(R.id.phone_update_phone)
    TextView phoneUpdatePhone;
    @BindView(R.id.phone_update_send)
    Button phoneUpdateSend;
    @BindView(R.id.phone_update_captcha)
    EditText phoneUpdateCaptcha;
    private CounterDisposableObserver observer;

    @Override
    protected void bindView(Bundle savedInstanceState) {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone_update;
    }

    public void nextUpdatePhone(View view) {
        Intent intent = new Intent(getCtx(), ModifyPhoneActivity.class);
        startActivity(intent);
    }

    public void sendCaptchaToUpdate(View view) {
        observer = new CounterDisposableObserver(phoneUpdateSend);
        RxUtil.countdown(60).subscribe(observer);
    }
}