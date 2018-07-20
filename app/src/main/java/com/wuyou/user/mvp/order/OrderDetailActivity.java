package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.OrderDetailServeBean;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.event.OrderEvent;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.CommentActivity;
import com.wuyou.user.view.activity.HelpRobotActivity;
import com.wuyou.user.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderDetailActivity extends BaseActivity<OrderContract.View, OrderContract.Presenter> implements OrderContract.View {
    @BindView(R.id.order_detail_status)
    TextView orderDetailStatus;
    @BindView(R.id.order_detail_un_pay_warn)
    TextView orderDetailWarn;
    @BindView(R.id.order_detail_address)
    TextView orderDetailAddress;
    @BindView(R.id.order_detail_name)
    TextView orderDetailName;
    @BindView(R.id.order_detail_phone)
    TextView orderDetailPhone;
    @BindView(R.id.order_detail_create_time)
    TextView orderDetailCreateTime;
    @BindView(R.id.order_detail_number)
    TextView orderDetailNumber;
    @BindView(R.id.order_detail_pay_method)
    TextView orderDetailPayMethod;
    @BindView(R.id.order_detail_pay_serial)
    TextView orderDetailBillSerial;
    @BindView(R.id.order_detail_action)
    TextView orderDetailAction;
    @BindView(R.id.order_detail_serve_way)
    TextView orderDetailServeWay;
    @BindView(R.id.order_detail_serve_time)
    TextView orderDetailServeTime;
    @BindView(R.id.order_detail_remark)
    TextView orderDetailRemark;
    @BindView(R.id.order_detail_pay_time)
    TextView orderDetailPayTime;
    @BindView(R.id.order_detail_contact_store)
    TextView orderDetailContactStore;
    @BindView(R.id.order_detail_second_payment)
    TextView orderDetailSecondPayment;
    @BindView(R.id.order_detail_cancel)
    TextView orderDetailCancel;
    @BindView(R.id.order_detail_store_name)
    TextView orderDetailStoreName;
    @BindView(R.id.order_detail_fee)
    TextView orderDetailFee;
    @BindView(R.id.order_detail_other_fee)
    TextView orderDetailOtherFee;
    @BindView(R.id.order_detail_amount)
    TextView orderDetailAmount;
    @BindView(R.id.order_detail_pay_area)
    LinearLayout orderDetailPayArea;
    @BindView(R.id.order_detail_bottom)
    LinearLayout orderDetailBottom;
    @BindView(R.id.order_detail_second_pay_time)
    TextView getOrderDetailSecondPayTime;
    @BindView(R.id.order_detail_serve_list)
    RecyclerView orderDetailServeList;
    private String orderId;
    private OrderBeanDetail beanDetail;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        setTitleText(R.string.order_detail);
        getStatusData();
    }

    @Override
    protected void getStatusData() {
        super.getStatusData();
        mPresenter.getOrderDetail(orderId);
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
    public void finishOrderSuccess() {
        EventBus.getDefault().post(new OrderEvent());
        finish();
    }

    @Override
    public void cancelSuccess(int position) {
        EventBus.getDefault().post(new OrderEvent());
        ToastUtils.ToastMessage(getCtx(), R.string.cancel_success);
        finish();
    }

    @Override
    public void showError(String message, int res) {
        baseStatusLayout.showErrorView(message);
    }

    @Override
    public void getOrderDetailSuccess(OrderBeanDetail bean) {
        baseStatusLayout.showContentView();
        setData(bean);
    }

    float serveAmount = 0;
    float visitingFee = 0;

    public void setData(OrderBeanDetail data) {
        beanDetail = data;
        if (beanDetail.status == 1) orderDetailWarn.setVisibility(View.VISIBLE);
        orderDetailStatus.setText(CommonUtil.getOrderStatusString(this,data.status));
        orderDetailStoreName.setText(data.shop.shop_name);

        orderDetailAmount.setText(CommonUtil.formatPrice(data.total_amount));
        orderDetailName.setText(data.address.name);
        orderDetailAddress.setText(String.format("%s%s%s%s", data.address.city_name, data.address.district, data.address.area, data.address.address));
        orderDetailPhone.setText(data.address.mobile);
        orderDetailCreateTime.setText(TribeDateUtils.dateFormat(new Date(data.created_at * 1000)));
        orderDetailNumber.setText(data.order_number);
        orderDetailServeWay.setText(data.service_mode);
        orderDetailServeTime.setText(data.service_date + "  " + data.service_time);
        orderDetailRemark.setText(data.remark);
        if (!TextUtils.isEmpty(data.serial)) orderDetailBillSerial.setText(data.serial);
        orderDetailPayMethod.setText(data.pay_type);
        orderDetailPayTime.setText(TribeDateUtils.dateFormat(new Date(data.pay_time * 1000)));
        if (data.second_pay_time!=0){
            findViewById(R.id.order_detail_second_pay_time_layout).setVisibility(View.VISIBLE);
            getOrderDetailSecondPayTime.setText(TribeDateUtils.dateFormat(new Date(data.second_pay_time * 1000)));
        }
        if (data.services != null) {
            LinearLayoutManager layout = new LinearLayoutManager(this);
            layout.setAutoMeasureEnabled(true);
            orderDetailServeList.setLayoutManager(layout);
            orderDetailServeList.setAdapter(new BaseQuickAdapter<OrderDetailServeBean, BaseHolder>(R.layout.item_order_detail_serve, data.services) {
                @Override
                protected void convert(BaseHolder baseHolder, OrderDetailServeBean serveBean) {
                    float price;
                    if (serveBean.has_specification == 1) {
                        price = serveBean.specification.price;
                        baseHolder.setText(R.id.order_detail_goods_specification, String.format("规格：%s", serveBean.specification.name));
                    } else {
                        price = serveBean.price;
                    }
                    View secondPaymentFlag = baseHolder.getView(R.id.order_detail_second_payment_flag);
                    if (serveBean.stage == 1) {
                        visitingFee = serveBean.visiting_fee;
                        secondPaymentFlag.setVisibility(View.GONE);
                    } else {
                        secondPaymentFlag.setVisibility(View.VISIBLE);
                    }
                    serveAmount += price * serveBean.number;
                    baseHolder.setText(R.id.order_detail_serve_name, serveBean.service_name)
                            .setText(R.id.order_detail_goods_number, serveBean.number + "")
                            .setText(R.id.order_detail_goods_fee, CommonUtil.formatPrice(price));
                    ImageView image = baseHolder.getView(R.id.order_detail_picture);
                    GlideUtils.loadRoundCornerImage(mContext, serveBean.image, image);
                    if (baseHolder.getAdapterPosition() == data.services.size() - 1) {
                        orderDetailOtherFee.setText(CommonUtil.formatPrice(visitingFee));
                        orderDetailFee.setText(CommonUtil.formatPrice(serveAmount));
                        orderDetailAmount.setText(CommonUtil.formatPrice(serveAmount + visitingFee));
                    }
                }
            });
        }
        setActionStatus();
    }


    @OnClick({R.id.order_detail_action, R.id.order_detail_contact_store, R.id.order_detail_cancel})
    public void onViewClicked(View view) {
        if (TextUtils.isEmpty(orderId)) return;
        switch (view.getId()) {
            case R.id.order_detail_contact_store:
                Intent intent = new Intent(getCtx(), HelpRobotActivity.class);
                startActivity(intent);
                break;
            case R.id.order_detail_action:
                switch (beanDetail.status) {
                    case 1:
                        payOrder();
                        break;
                    case 3:
                        Intent intent1 = new Intent(getCtx(), CommentActivity.class);
                        OrderBean orderBean = beanDetail;
                        intent1.putExtra(Constant.ORDER_BEAN, orderBean);
                        intent1.putExtra(Constant.SERVE_ID, beanDetail.services.get(0).service_id);
                        startActivity(intent1);
                        break;
                    case 2:
                        mPresenter.finishOrder(orderId);
                        break;
                }
                break;
            case R.id.order_detail_cancel:
                new CustomAlertDialog.Builder(getCtx()).setTitle(R.string.prompt).setMessage("确认取消?")
                        .setPositiveButton(getCtx().getString(R.string.yes), (dialog, which) -> {
                            showLoadingDialog();
                            mPresenter.cancelOrder(0, orderId);
                        }).setNegativeButton(getCtx().getResources().getString(R.string.cancel), null).create().show();
                break;
        }
    }

    private void payOrder() {
        Intent intent = new Intent(getCtx(), PayChooseActivity.class);
        intent.putExtra(Constant.ORDER_ID, beanDetail.order_id);
        intent.putExtra(Constant.SECOND_PAY, 1);
        startActivity(intent);
    }

    public void setActionStatus() {
        switch (beanDetail.status) {
            case 1:
                orderDetailStatus.setTextColor(getResources().getColor(R.color.common_orange));
                orderDetailPayArea.setVisibility(View.GONE);
                orderDetailCancel.setVisibility(View.VISIBLE);
                orderDetailContactStore.setVisibility(View.VISIBLE);
                orderDetailAction.setVisibility(View.VISIBLE);
                break;
            case 2:
                orderDetailStatus.setTextColor(getResources().getColor(R.color.common_orange));
                orderDetailPayArea.setVisibility(View.VISIBLE);
                orderDetailCancel.setVisibility(View.GONE);
                if (beanDetail.can_finish == 1) {
                    orderDetailContactStore.setVisibility(View.GONE);
                    orderDetailAction.setVisibility(View.VISIBLE);
                    orderDetailAction.setText(R.string.finish_serve);
                } else {
                    orderDetailAction.setVisibility(View.GONE);
                    orderDetailContactStore.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                orderDetailStatus.setTextColor(getResources().getColor(R.color.common_green));
                orderDetailAction.setText(R.string.go_comment);
                orderDetailAction.setBackgroundResource(R.drawable.white_border);
                orderDetailAction.setVisibility(View.VISIBLE);
                orderDetailCancel.setVisibility(View.GONE);
                orderDetailContactStore.setVisibility(View.GONE);
                break;
            case 4:
                orderDetailStatus.setTextColor(getResources().getColor(R.color.gray_99));
                orderDetailAction.setVisibility(View.GONE);
                orderDetailCancel.setVisibility(View.GONE);
                break;
            case 5:
                orderDetailStatus.setTextColor(getResources().getColor(R.color.gray_99));
                orderDetailPayArea.setVisibility(View.GONE);
                orderDetailBottom.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent();
        intent.setClass(getCtx(), MainActivity.class);
        intent.putExtra(Constant.MAIN_FLAG, 1);
        startActivity(intent);
        finish();
    }
}