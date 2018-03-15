package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.OrderIdBean;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.mvp.address.AddressAddActivity;
import com.wuyou.user.mvp.order.OrderAddressActivity;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class NewOrderActivity extends BaseActivity {
    @BindView(R.id.create_order_serve_time)
    TextView createOrderServeTime;
    @BindView(R.id.create_order_comment)
    EditText createOrderComment;
    @BindView(R.id.create_order_fee)
    TextView createOrderFee;
    @BindView(R.id.create_order_other_fee)
    TextView createOrderDoorFee;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.create_order_address_person)
    TextView createOrderAddressPerson;
    @BindView(R.id.create_order_address_detail)
    TextView createOrderAddressDetail;
    @BindView(R.id.create_order_address_phone)
    TextView createOrderAddressPhone;
    @BindView(R.id.create_order_site_name)
    TextView createOrderSiteName;
    @BindView(R.id.create_order_goods_picture)
    ImageView createOrderGoodsPicture;
    @BindView(R.id.create_order_goods_name)
    TextView createOrderGoodsName;
    @BindView(R.id.create_order_goods_standard)
    TextView createOrderGoodsStandard;
    @BindView(R.id.create_order_goods_number)
    TextView createOrderGoodsNumber;
    @BindView(R.id.create_order_serve_way)
    TextView createOrderServeWay;
    @BindView(R.id.create_order_button)
    Button createOrderButton;
    private ServeDetailBean bean;

    private AddressBean defaultAddress;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.create_order_address_area).requestFocus();
        Intent intent = getIntent();
        bean = intent.getParcelableExtra(Constant.SERVE_BEAN);
        if (bean != null) {
            createOrderGoodsName.setText(bean.name);
//            createOrderServeTime.setText(bean.service_time);
            createOrderFee.setText(bean.price);
            createOrderDoorFee.setText(bean.other_price);
            GlideUtils.loadRoundCornerImage(this,bean.image,createOrderGoodsPicture,8);
        }
        defaultAddress = CarefreeDaoSession.getInstance().getDefaultAddress();
        setAddressInfo();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    public void doCreateOrder(View view) {
        if (defaultAddress == null) {
            ToastUtils.ToastMessage(getCtx(), "请确认地址");
            return;
        }
        normalCreateOrder();
    }

    private void normalCreateOrder() {
        if (bean.service_id != null) bean.id = bean.service_id;
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .createOrder(CarefreeDaoSession.getInstance().getUserId(),QueryMapBuilder.getIns().put("address", defaultAddress.city_name + defaultAddress.district + defaultAddress.address)
                        .put("remark", createOrderComment.getText().toString().trim())
                        .put("service_id", bean.id)
                        .put("address_id",defaultAddress.id)
                        .put("service_time", bean.service_time)
                        .put("service_mode",createOrderServeWay.getText().toString().trim())
                        .put("number",createOrderGoodsNumber.getText().toString().trim())
                        .put("total_amount", Float.parseFloat(bean.price) + Float.parseFloat(bean.other_price) + "")
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

    @OnClick({R.id.create_order_address_add, R.id.create_order_address_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_order_address_area:
                Intent intent = new Intent(getCtx(), OrderAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.create_order_address_add:
                Intent intent1 = new Intent(getCtx(), AddressAddActivity.class);
                startActivityForResult(intent1, 201);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 203) {
            defaultAddress = data.getParcelableExtra(Constant.ADDRESS_BEAN);
        } else if (resultCode == 204) {
            defaultAddress = data.getParcelableExtra(Constant.ADDRESS_RESULT);
        }
        setAddressInfo();
    }

    public void setAddressInfo() {
        if (defaultAddress == null) return;
        findViewById(R.id.create_order_address_add).setVisibility(View.GONE);
        createOrderAddressPerson.setText(defaultAddress.name);
        createOrderAddressDetail.setText(defaultAddress.city_name + defaultAddress.district + defaultAddress.address);
        createOrderAddressPhone.setText(defaultAddress.mobile);
    }
}
