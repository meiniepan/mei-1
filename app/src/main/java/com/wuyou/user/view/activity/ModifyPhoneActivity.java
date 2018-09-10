package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.CounterDisposableObserver;
import com.wuyou.user.util.RxUtil;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/5/8.
 */

public class ModifyPhoneActivity extends BaseActivity {
    @BindView(R.id.phone_update_phone)
    EditText phoneUpdatePhone;
    @BindView(R.id.phone_update_send)
    Button phoneUpdateSend;
    @BindView(R.id.phone_update_captcha)
    EditText phoneUpdateCaptcha;
    private CounterDisposableObserver observer;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.bind_new_phone);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone_update;
    }

    public void nextUpdatePhone(View view) {
        if (phoneUpdateCaptcha.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), R.string.input_captcha);
            return;
        }
        showLoadingDialog();
        final String phone = phoneUpdatePhone.getText().toString().trim();
        if (!CommonUtil.checkPhone("", phone, this)) return;
        CarefreeRetrofit.getInstance().createApi(UserApis.class).updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                .put("field", "mobile")
                .put("value", phone)
                .put("captcha", phoneUpdateCaptcha.getText().toString().trim()).buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), R.string.update_success);
                        setResult(RESULT_OK, new Intent().putExtra("info", phone));
                        finish();
                    }
                });
    }

    public void sendCaptchaToUpdate(View view) {
        if (phoneUpdatePhone.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), R.string.input_phone);
            return;
        }
        String phone = phoneUpdatePhone.getText().toString().trim();
        if (!CommonUtil.checkPhone("", phone, this)) return;
        phoneUpdateCaptcha.requestFocus();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getCaptchaCode(QueryMapBuilder.getIns().put("mobile", phone).put("type", "3").buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                    }
                });
        observer = new CounterDisposableObserver(phoneUpdateSend);
        RxUtil.countdown(60).subscribe(observer);
    }
}