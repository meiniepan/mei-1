package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.wuyou.user.R;
import com.wuyou.user.bean.BankCard;
import com.wuyou.user.bean.PayChannel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2017/1/3.
 */

public class PayChoosePanel extends Dialog implements View.OnClickListener {
    Context mCtx;
    @BindView(R.id.new_order_pay_balance)
    RadioButton rbBalance;
    @BindView(R.id.new_order_pay_wx)
    RadioButton rbWeChat;
    @BindView(R.id.new_order_pay_ali)
    RadioButton rbAli;
    @BindView(R.id.card_list)
    ListView cardList;
    @BindView(R.id.pay_choose_available_balance)
    TextView tvBalance;

    @BindView(R.id.ll_add__bank_card)
    LinearLayout addBankCard;

    private PayChannel payMethod = PayChannel.BALANCE;
    private onChooseFinish onChooseFinish;
    private BankCard mBankCard;
    private String bankName;

    public PayChoosePanel(Context context, double availableBalance, onChooseFinish onChooseFinish) {
        super(context, R.style.pay_dialog);
        mCtx = context;
        this.onChooseFinish = onChooseFinish;
        initView();
        initData();
        if (availableBalance == -1) {//充值
            findViewById(R.id.ll_balance).setVisibility(View.GONE);
        } else {
            tvBalance.setText(availableBalance + "");
        }
    }

    private void initView() {
        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.pay_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mCtx, 400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.ll_balance).setOnClickListener(this);
        rootView.findViewById(R.id.ll_wx).setOnClickListener(this);
        rootView.findViewById(R.id.ll_ali).setOnClickListener(this);
        rootView.findViewById(R.id.ll_add__bank_card).setOnClickListener(this);
        rootView.findViewById(R.id.pay_choose_close).setOnClickListener(this);
    }

    private void initData() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_balance:
                rbBalance.setChecked(true);
                rbWeChat.setChecked(false);
                rbAli.setChecked(false);
                payMethod = PayChannel.BALANCE;
                onChooseFinish.onChoose(payMethod, null, bankName);
                dismiss();
                break;
            case R.id.ll_wx:
                rbBalance.setChecked(false);
                rbWeChat.setChecked(true);
                rbAli.setChecked(false);
                payMethod = PayChannel.WECHAT;
                onChooseFinish.onChoose(payMethod, null, bankName);
                dismiss();
            case R.id.ll_ali:
                rbBalance.setChecked(false);
                rbWeChat.setChecked(false);
                rbAli.setChecked(true);
                payMethod = PayChannel.ALIPAY;
                onChooseFinish.onChoose(payMethod, null, bankName);
                dismiss();
                break;
            case R.id.ll_add__bank_card:
                dismiss();
                break;
            case R.id.pay_choose_close:
                dismiss();
                break;
        }
    }


    public interface onChooseFinish {
        void onChoose(PayChannel payChannel, BankCard bankCard, String bankName);
    }
}
