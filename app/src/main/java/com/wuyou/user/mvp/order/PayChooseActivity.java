package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.response.WxPayResponse;
import com.wuyou.user.event.OrderEvent;
import com.wuyou.user.event.WXPayEvent;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.PayFinishActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/5/14.
 */

public class PayChooseActivity extends BaseActivity {
    @BindView(R.id.ll_ali)
    LinearLayout llAli;
    private String orderId;
    private String secondPay = "1";
    private int backFlag;
    private float totalPrice;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.choose_pay_method);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        backFlag = intent.getIntExtra(Constant.BACK_FLAG, 0);
        orderId = intent.getStringExtra(Constant.ORDER_ID);
        secondPay = intent.getIntExtra(Constant.SECOND_PAY, 1) + "";
        if (intent.getBooleanExtra(Constant.FROM_WEB, false)) {
            llAli.setVisibility(View.GONE);
        }
        totalPrice = intent.getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (backFlag == 1) {
            EventBus.getDefault().post(new OrderEvent());
            Intent intent = new Intent(getCtx(), OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, orderId);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_choose;
    }

    private IWXAPI msgApi;

    private void payInWX() {
        msgApi = WXAPIFactory.createWXAPI(CarefreeApplication.getInstance().getApplicationContext(), null);
        msgApi.registerApp(Constant.WX_ID);
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getWXPayOrderInfo(orderId, QueryMapBuilder.getIns()
                .put("uid", CarefreeDaoSession.getInstance().getUserId())
                .put("pay_type", "APP")
                .put("is_mini_program", "0")
                .put("stage", secondPay).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WxPayResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<WxPayResponse> baseResponseBaseResponse) {
                        doPay(baseResponseBaseResponse.data);
                    }
                });

    }

    private void doPay(WxPayResponse data) {
        PayReq request = new PayReq();
        request.appId = data.appid;
        request.partnerId = data.mch_id;
        request.prepayId = data.prepay_id;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.nonce_str;
        request.timeStamp = data.timestamp;
        request.sign = data.sign;
        msgApi.sendReq(request);
    }


    private void payInAli() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                .getAliPayOrderInfo(orderId, QueryMapBuilder.getIns().put("uid", CarefreeDaoSession.getInstance().getUserId()).put("stage", secondPay).buildGet())
                .subscribeOn(Schedulers.io())
                .map(simpleResponse -> {
                    PayTask alipay = new PayTask(this);
                    return alipay.payV2(simpleResponse.data.response, true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> doNext());
    }

    private void pfaInAli() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                .getAliPayOrderInfo(orderId, QueryMapBuilder.getIns().put("uid", CarefreeDaoSession.getInstance().getUserId()).put("stage", secondPay)
                        .put("trade_type", "NATIVE").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fpaNext(response.data.response, 0));
    }

    private void pfaInWX() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getWXPayOrderInfo(orderId, QueryMapBuilder.getIns()
                .put("uid", CarefreeDaoSession.getInstance().getUserId())
                .put("is_mini_program", "0")
                .put("stage", secondPay)
                .put("pay_type", "NATIVE").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fpaNext(response.data.code_url, 1));
    }

    private void fpaNext(String response, int isWX) {
        Intent intent = new Intent(getCtx(), ProceedsQrActivity.class);
        intent.putExtra(Constant.CHOSEN_SERVICE_TOTAL, totalPrice);
        if (isWX == 0) {
            intent.putExtra(Constant.EXTRA_PAY_WAY, "支付宝");
        } else {
            intent.putExtra(Constant.EXTRA_PAY_WAY, "微信");
        }
        intent.putExtra(Constant.PROCEEDS_QR, response);
        intent.putExtra(Constant.ORDER_ID, orderId);
        startActivity(intent);
        finish();
    }


    private void doNext() {
        EventBus.getDefault().post(new OrderEvent());
        Intent intent = new Intent(getCtx(), PayFinishActivity.class);
        intent.putExtra(Constant.ORDER_ID, orderId);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXPayFinish(WXPayEvent event) {
        if (event.errCode == 0) {
            Intent intent = new Intent();
            intent.putExtra(Constant.STATUS_CODE, 1);
            setResult(RESULT_OK);
            doNext();
        }
    }

    //代付逻辑
    private void goNext() {
//        showLoadingDialog();
//        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
//                .getExtraPayQr(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserInfo().getWorker_id()).put("payment_channel", payWay).buildGet())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseResponse<QrEntity>>() {
//                    @Override
//                    public void onSuccess(BaseResponse<QrEntity> response) {
//                        String qrString = response.data.qr_code;
//                        Intent intent = new Intent(getCtx(), ProceedsQrActivity.class);
//                        intent.putExtra(Constant.CHOSEN_SERVICE_TOTAL, total);
//                        if (payWay.equals("1")) {
//                            intent.putExtra(Constant.EXTRA_PAY_WAY, "支付宝");
//                        } else {
//                            intent.putExtra(Constant.EXTRA_PAY_WAY, "微信");
//                        }
//
//                        intent.putExtra(Constant.PROCEEDS_QR, qrString);
//                        intent.putExtra(Constant.ORDER_ID, orderId);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
    }


    @OnClick({R.id.ll_ali, R.id.ll_wx, R.id.ll_zfb_df, R.id.ll_wx_df})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_ali:
                payInAli();
                break;
            case R.id.ll_wx:
                payInWX();
                break;
            case R.id.ll_zfb_df:
                pfaInAli();
                break;
            case R.id.ll_wx_df:
                pfaInWX();
                break;
        }
    }
}
