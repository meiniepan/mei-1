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
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
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
    @BindView(R.id.serve_category)
    TextView serveCategory;
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
    @BindView(R.id.create_order_door_fee)
    TextView createOrderDoorFee;

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    public void doCreateOrder(View view) {
        if (createOrderAddress.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "");
            return;
        }
        if (createOrderOwner.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "");
            return;
        }

        String fee = createOrderFee.getText().toString().trim();
        String otherFee = createOrderDoorFee.getText().toString().trim();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .createOrder(QueryMapBuilder.getIns().put("address", createOrderAddress.getText().toString().trim())
                        .put("username", createOrderOwner.getText().toString().trim())
                        .put("mobile", CarefreeApplication.getInstance().getUserInfo().getPhone())
//                        .put("service_time",0+"")
                        .put("remark", createOrderComment.getText().toString().trim())
                        .put("service_price", fee)
                        .put("orther_price", otherFee)
                        .put("total_price", Float.parseFloat(fee) + Float.parseFloat(otherFee) + "")
                        .put("user_id", CarefreeApplication.getInstance().getUserInfo().getUid())
//                        .put("shop_id",???)//TODO
//                        .put("service_id,????")
//                        .put("nums",????)
                        .buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> baseResponse) {
                        createSuccess(baseResponse.data);
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
    }
}
