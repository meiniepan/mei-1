package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.OrderPreferentialBean;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderDetailActivity extends BaseActivity<OrderContract.View, OrderContract.Presenter> implements OrderContract.View {
    @BindView(R.id.order_detail_status)
    TextView orderDetailStatus;
    @BindView(R.id.order_detail_date)
    TextView orderDetailDate;
    @BindView(R.id.order_detail_store)
    TextView orderDetailStore;
    @BindView(R.id.order_detail_title)
    TextView orderDetailTitle;
    @BindView(R.id.order_detail_count)
    TextView orderDetailCount;
    @BindView(R.id.order_detail_price)
    TextView orderDetailPrice;
    @BindView(R.id.order_detail_discount_name)
    TextView orderDetailDiscountName;
    @BindView(R.id.order_detail_discount)
    TextView orderDetailDiscount;
    @BindView(R.id.order_detail_price_final)
    TextView orderDetailPriceFinal;
    @BindView(R.id.order_detail_address)
    TextView orderDetailAddress;
    @BindView(R.id.order_detail_name)
    TextView orderDetailName;
    @BindView(R.id.order_detail_phone)
    TextView orderDetailPhone;
    @BindView(R.id.order_detail_recent)
    TextView orderDetailRecent;
    @BindView(R.id.order_detail_create_time)
    TextView orderDetailCreateTime;
    @BindView(R.id.order_detail_number)
    TextView orderDetailNumber;
    @BindView(R.id.order_detail_amount)
    TextView orderDetailAmount;
    @BindView(R.id.order_detail_pay_method)
    TextView orderDetailPayMethod;
    @BindView(R.id.order_detail_bill_status)
    TextView orderDetailBillStatus;
    @BindView(R.id.order_detail_cancel)
    TextView orderDetailCancel;
    private String orderId;
    private String shopTel;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        mPresenter.getOrderDetail(orderId);
        showLoadingDialog();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    public void getOrderSuccess(OrderListResponse list) {
    }

    @Override
    public void loadMore(OrderListResponse data) {
    }

    @Override
    public void loadMoreFail(String displayMessage, int code) {
    }

    @Override
    public void paySuccess() {

    }

    @Override
    public void finishOrderSuccess() {

    }

    @Override
    public void cancelSuccess(int position) {
        ToastUtils.ToastMessage(getCtx(), R.string.cancel_success);
        finish();
    }

    @Override
    public void getOrderDetailSuccess(OrderBeanDetail bean) {
        setData(bean);
    }

    public void setData(OrderBeanDetail data) {
        orderDetailStatus.setText(data.status);
        if (data.updated_at == 0) data.updated_at = data.created_at;
        orderDetailDate.setText(TribeDateUtils.dateFormat(new Date(data.updated_at * 1000)));
        orderDetailAddress.setText(data.contact_address);
        orderDetailStore.setText(data.shop_name);
        orderDetailTitle.setText(data.category);
        orderDetailCount.setText(data.nums);
        orderDetailPrice.setText(data.price);
        if (data.preferentials != null && data.preferentials.size() > 0) {
            OrderPreferentialBean orderPreferentialBean = data.preferentials.get(0);
            orderDetailDiscountName.setText(orderPreferentialBean.preferential_name);
            orderDetailDiscount.setText(orderPreferentialBean.preferential_price);
        }
        orderDetailPriceFinal.setText(data.payment);
        orderDetailName.setText(data.contact_name);
        orderDetailRecent.setText(data.service_time);
        orderDetailCreateTime.setText(TribeDateUtils.dateFormat(new Date(data.created_at * 1000)));
        orderDetailNumber.setText(data.order_no);
        orderDetailAmount.setText(data.payment);
        orderDetailBillStatus.setText(data.pay_status);
        orderDetailPayMethod.setText(data.pay_type);
        orderDetailPhone.setText(data.contact_tel);

        shopTel = data.shop_tel;
    }

    @OnClick({R.id.order_detail_cancel, R.id.order_detail_contact_store})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_detail_contact_store:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopTel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.order_detail_cancel:
                new CustomAlertDialog.Builder(getCtx()).setTitle(R.string.prompt).setMessage(getCtx().getString(R.string.complete))
                        .setPositiveButton(getCtx().getString(R.string.complete), (dialog, which) -> {
                            mPresenter.cancelOrder(0, orderId);
                        }).setNegativeButton(getCtx().getResources().getString(R.string.cancel), null).create().show();
                break;
        }
    }
}