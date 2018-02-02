package com.wuyou.user.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.widget.CaptchaEditText;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn91 on 2018/2/2.
 */

public class CaptchaInputActivity extends BaseActivity {
    @BindView(R.id.input_captcha_edit)
    CaptchaEditText inputCaptchaEdit;
    @BindView(R.id.input_captcha_re_send)
    Button reSendCaptcha;
    private DisposableObserver<Integer> observer;
    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra(Constant.PHONE);
        int flag = getIntent().getIntExtra(Constant.INPUT_PHONE_FLAG, 0);
        observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer value) {
                reSendCaptcha.setText(value + "秒后重发");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                reSendCaptcha.setText(getString(R.string.send_captcha));
            }
        };
        RxUtil.countdown(59).subscribe(observer);

        inputCaptchaEdit.setInputCompleteListener(() -> {
            if (flag==0){ //reset password
                jump(inputCaptchaEdit.getStrPassword());
            }else { //register
                doLogin(inputCaptchaEdit.getStrPassword());
            }
        });
    }

    private void jump(String inputCaptcha) {
        Intent view = new Intent(getCtx(),CompletingInfoActivity.class);
        startActivity(view);
        
    }

    private void doLogin(String inputCaptcha) {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .doLogin(QueryMapBuilder.getIns().put("mobile", phone).put("captcha", inputCaptcha).buildPost())
                .subscribeOn(Schedulers.io())
                .flatMap(userInfoBaseResponse -> CarefreeRetrofit.getInstance().createApi(UserApis.class)
                        .getUserInfo(userInfoBaseResponse.data.getUid(), QueryMapBuilder.getIns().buildGet()))
                .doOnNext(userInfoBaseResponse -> CarefreeApplication.getInstance().setUserInfo(userInfoBaseResponse.data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse){
                    Intent view = new Intent(getCtx(), CompletingInfoActivity.class);
                    startActivity(view);
                }
    });
}

    @Override
    protected int getContentLayout() {
        return R.layout.activity_captcha_input;
    }

    public void re_send(View view) {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getVerifyCode(QueryMapBuilder.getIns().put("phone", phone).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        RxUtil.countdown(59).subscribe(observer);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (observer != null) {
            observer.dispose();
        }
    }
}
