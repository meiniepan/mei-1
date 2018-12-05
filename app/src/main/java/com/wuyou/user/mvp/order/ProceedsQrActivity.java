package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.response.SimpleResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.zxing.encoding.QRCode;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.PayFinishActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/10.
 */

public class ProceedsQrActivity extends BaseActivity {
    @BindView(R.id.tv_proceeds_sum)
    TextView tvProceedsSum;
    @BindView(R.id.iv_proceeds_qr)
    ImageView ivProceedsQr;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    String qrString;
    float total;
    String payWay;
    String orderId;
    private Disposable mDisposable;
    private boolean fromActivity;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        qrString = getIntent().getStringExtra(Constant.PROCEEDS_QR);
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        total = getIntent().getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0F);
        payWay = getIntent().getStringExtra(Constant.EXTRA_PAY_WAY);
        fromActivity = getIntent().getBooleanExtra(Constant.FROM_WEB, false);
        setTitleText("收款二维码");
        tvPayType.setText(payWay);
        tvProceedsSum.setText(CommonUtil.formatPrice(total));
        ivProceedsQr.setImageBitmap(QRCode.createQRCode(qrString));
        if (fromActivity) {
            queryActivityPayResult();
        } else {
            queryPayResult();
        }
    }

    private void queryActivityPayResult() {
        mDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .flatMap(aLong -> CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                        .getActivityPayStatus(orderId, QueryMapBuilder.getIns().put("user_id", CarefreeDaoSession.getInstance().getUserId()).buildGet()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<BaseResponse<SimpleResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<SimpleResponse> response) {
                        if (response.data.is_paid == 1) {
                            setResult();
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }
                });
    }

    private void queryPayResult() {
        mDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .flatMap(aLong -> CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                        .getPayStatus(orderId, QueryMapBuilder.getIns().buildGet()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<BaseResponse<SimpleResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<SimpleResponse> response) {
                        if (response.data.is_paid == 1) {
                            setResult();
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }
                });
    }

    private void setResult() {
        if (!fromActivity) {
            Intent intent = new Intent(getCtx(), PayFinishActivity.class);
            intent.putExtra(Constant.ORDER_ID, orderId);
            startActivity(intent);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }
}
