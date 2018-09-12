package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CaptchaEditText;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by DELL on 2018/9/12.
 */

public class CaptchaPanel extends Dialog {
    @BindView(R.id.captcha_dialog_phone)
    TextView captchaDialogPhone;
    @BindView(R.id.captcha_dialog_captcha)
    CaptchaEditText captchaDialogCaptcha;
    @BindView(R.id.captcha_dialog_countdown)
    Button captchaCountdown;

    private DisposableObserver<Integer> observer;

    public CaptchaPanel(@NonNull Context context, OnCheckSuccess onCheckSuccess) {
        super(context, R.style.sheet_dialog);
        this.onCheckSuccess = onCheckSuccess;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.captcha_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DensityUtils.dip2px(getContext(), 315);
        params.height = DensityUtils.dip2px(getContext(), 230);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        captchaDialogPhone.setText(CommonUtil.getPhoneWithStar(CarefreeDaoSession.getInstance().getUserInfo().getMobile()));
        captchaDialogCaptcha.setTextBackground(R.drawable.gray_border);
        captchaDialogCaptcha.setTextColor(R.color.common_dark);
        captchaDialogCaptcha.setTextSize(14);
        captchaDialogCaptcha.showKeyBoard();
        captchaDialogCaptcha.setInputCompleteListener(() -> {
            checkCaptcha();
            captchaDialogCaptcha.dismissKeyBoard();
        });

        startCountDown();
    }

    private void startCountDown() {
        getCaptcha();
        observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                captchaCountdown.setText(integer + "s");
            }

            @Override
            public void onError(Throwable e) {
                captchaCountdown.setEnabled(true);
                captchaCountdown.setText("发送验证码");
                observer.dispose();
            }

            @Override
            public void onComplete() {
                captchaCountdown.setEnabled(true);
                captchaCountdown.setText("发送验证码");
                observer.dispose();
            }
        };
        RxUtil.countdown(Constant.COUNT_DOWN).subscribe(observer);
    }

    private void getCaptcha() {
        CarefreeRetrofit.getInstance().createApi(UserApis.class).getCaptchaForCheck(Constant.CAPTCHA_UNLOCK_ACCOUNT, QueryMapBuilder.getIns().put("mobile", CarefreeDaoSession.getInstance().getUserInfo().getMobile()).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        observer.onComplete();
                    }
                });
    }

    private void checkCaptcha() {
        if (captchaDialogCaptcha.getStrPassword().length() == 0) {
            ToastUtils.ToastMessage(getContext(), R.string.edit_verify);
            return;
        }
        CarefreeRetrofit.getInstance().createApi(UserApis.class).checkCaptcha(Constant.CAPTCHA_CHECK_UNLOCK_ACCOUNT, QueryMapBuilder.getIns().put("mobile", CarefreeDaoSession.getInstance().getUserInfo().getMobile()).put("captcha", captchaDialogCaptcha.getStrPassword()).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (onCheckSuccess != null) onCheckSuccess.onSuccess();
                        dismiss();
                    }
                });
    }

    @OnClick({R.id.captcha_dialog_countdown})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.captcha_dialog_countdown:
                startCountDown();
                break;
        }
    }

    private OnCheckSuccess onCheckSuccess;

    public interface OnCheckSuccess {
        void onSuccess();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (observer != null) {
            observer.dispose();
        }
    }
}
