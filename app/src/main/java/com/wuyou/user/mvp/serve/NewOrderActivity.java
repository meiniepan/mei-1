package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderIdBean;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class NewOrderActivity extends BaseActivity {
    @BindView(R.id.create_order_address)
    TextView createOrderAddress;
    @BindView(R.id.create_order_owner)
    TextView createOrderOwner;
    @BindView(R.id.create_order_phone)
    TextView createOrderPhone;
    @BindView(R.id.create_order_serve_time)
    TextView createOrderServeTime;
    @BindView(R.id.create_order_comment)
    EditText createOrderComment;
    @BindView(R.id.create_order_fee)
    TextView createOrderFee;
    @BindView(R.id.create_order_other_fee)
    TextView createOrderDoorFee;
    private ServeDetailBean bean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        bean = intent.getParcelableExtra(Constant.SERVE_BEAN);
        if (bean != null) {
            createOrderFee.setText(bean.price);
            createOrderServeTime.setText(bean.service_time);
            createOrderFee.setText(bean.price);
            createOrderDoorFee.setText(bean.other_price);
        }
        if (!checkUser(this)) return;
        createOrderPhone.setText(CarefreeApplication.getInstance().getUserInfo().getMobile());
        createOrderComment.requestFocus();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    public void doCreateOrder(View view) {
        if (createOrderAddress.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请选择地址");
            return;
        }
        if (createOrderOwner.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请输入姓名");
            return;
        }
        normalCreateOrder();
    }

    private void normalCreateOrder() {
        if (createOrderPhone.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请输入手机号");
            return;
        }
        if (createOrderAddress.length()==0){
            ToastUtils.ToastMessage(getCtx(), "请输入手机号");
            return;
        }
        if (createOrderAddress.length()==0){
            ToastUtils.ToastMessage(getCtx(), "请输入手机号");
            return;
        }
        if (bean.service_id != null) bean.id = bean.service_id;
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .createOrder(QueryMapBuilder.getIns().put("address", createOrderAddress.getText().toString().trim())
                        .put("username", createOrderOwner.getText().toString().trim())
                        .put("mobile", createOrderPhone.getText().toString().trim())
                        .put("service_time", bean.service_time)
                        .put("remark", createOrderComment.getText().toString().trim())
                        .put("service_price", bean.price)
                        .put("other_price", bean.other_price)
                        .put("total_price", Float.parseFloat(bean.price) + Float.parseFloat(bean.other_price) + "")
                        .put("user_id", CarefreeApplication.getInstance().getUserId())
                        .put("shop_id", bean.shop_id)
                        .put("service_id", bean.id)
                        .put("num", 1 + "")
                        .buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderIdBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderIdBean> baseResponse) {
                        createSuccess(baseResponse.data.order_id);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }
                });
    }

    private void createSuccess(String orderId) {
        Intent intent = new Intent(getCtx(), OrderDetailActivity.class);
        intent.putExtra(Constant.ORDER_ID, orderId);
        startActivity(intent);
        finish();
        AppManager.getAppManager().finishActivity(ServeDetailActivity.class);
        AppManager.getAppManager().finishActivity(FastCreateActivity.class);
        AppManager.getAppManager().finishActivity(ServeCategoryListActivity.class);
    }
}
