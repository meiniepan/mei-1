package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.bean.BankCard;
import com.wuyou.user.bean.PayChannel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog implements View.OnClickListener, PayChoosePanel.onChooseFinish {
    private final OnPayFinishListener onFinishListener;
    private Context mCtx;
    @BindView(R.id.pay_way)
    TextView tvWay;
    @BindView(R.id.pay_money)
    TextView tvTotal;
    @BindView(R.id.pay_choose_area)
    View chooseArea;
    @BindView(R.id.pay_choose)
    View arrow;

    private View rootView;
    private String totalFee;
    private String targetId;
    private String paymentType;
    private String ownerId;

    private PayChoosePanel payChoosePanel;


    public PayPanel(Context context, OnPayFinishListener onDismissListener) {
        super(context, R.style.my_dialog);
        mCtx = context;
        this.onFinishListener = onDismissListener;
        ownerId = CarefreeDaoSession.getInstance().getUserId();
//        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    public void setData(String price, String targetId, String type) {
        this.totalFee = price;
        tvTotal.setText(price);
        this.targetId = targetId;
        paymentType = type;
        getWalletInfo();
    }

    private void initView() {
        rootView = LayoutInflater.from(mCtx).inflate(R.layout.pay_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mCtx, 400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.pay_ask).setOnClickListener(this);
        rootView.findViewById(R.id.pay_close).setOnClickListener(this);
        rootView.findViewById(R.id.pay_finish).setOnClickListener(this);
        chooseArea.setOnClickListener(this);
    }


    private void getWalletInfo() {
    }


    private void showNotEnough(final float balance) {
        new CustomAlertDialog.Builder(mCtx).setTitle(R.string.prompt).setMessage(mCtx.getString(R.string.complete))
                .setPositiveButton(mCtx.getString(R.string.complete), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton(mCtx.getResources().getString(R.string.cancel), null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getContext()).setTitle(R.string.prompt).setMessage(R.string.not_set_pwd)
                .setPositiveButton("去设置", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton(mCtx.getString(R.string.cancel), null).create().show();
    }

//    private void showPasswordPanel(final String password) {
//        NewPasswordPanel passwordPanel = new NewPasswordPanel(mCtx, password, new NewPasswordPanel.OnPwdFinishListener() {
//            @Override
//            public void onPwdFinishListener(String strPassword) {
//                createPayment(strPassword);
//            }
//
//            @Override
//            public void onPwdPanelDismiss() {
//                dismiss();
//            }
//        });
//        passwordPanel.show();
//        TranslateAnimation animation = new TranslateAnimation(0, -CommonUtils.getScreenWidth(mCtx), 0, 0);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        animation.start();
//        rootView.startAnimation(animation);
//    }

    private void createPayment(final String password) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_close:
                dismiss();
                break;
            case R.id.pay_finish:
                ToastUtils.ToastMessage(getContext(), "支付成功");
                dismiss();
                onFinishListener.onPaySuccess();
                break;
            case R.id.pay_choose_area:
                payChoosePanel = new PayChoosePanel(mCtx, 0, this);
                payChoosePanel.show();
                break;
        }
    }

    @Override
    public void onChoose(PayChannel payChannel, BankCard bankCard, String bankName) {
        tvWay.setText(payChannel.value);
    }

    public interface OnPayFinishListener {
        void onPaySuccess();

        void onPayFail(ApiException e);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void paySuccess(WXPayEvent event) {
//        dismiss();
//        if (onFinishListener != null) onFinishListener.onPaySuccess();
//    }
}