package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.wuyou.user.bean.response.ServeTimeBean;
import com.wuyou.user.mvp.address.AddressAddActivity;
import com.wuyou.user.mvp.order.OrderAddressActivity;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.LinkagePicker;
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
            createOrderGoodsName.setText(bean.title);
            createOrderFee.setText(bean.price + "");
            createOrderDoorFee.setText(bean.visiting_fee + "");
            GlideUtils.loadRoundCornerImage(this, bean.photo, createOrderGoodsPicture, 8);
        }
        defaultAddress = CarefreeDaoSession.getInstance().getDefaultAddress();
        setAddressInfo();
        getServeTime(bean.service_id, bean.shop_id);
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
        if (TextUtils.isEmpty(serveDate)) {
            ToastUtils.ToastMessage(getCtx(), "请选择服务时间");
            return;
        }
        normalCreateOrder();
    }

    private void normalCreateOrder() {
        showLoadingDialog();
        if (bean.service_id != null) bean.id = bean.service_id;
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .createOrder(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("remark", createOrderComment.getText().toString().trim())
                        .put("service_id", bean.service_id)
                        .put("address_id", defaultAddress.id)
                        .put("service_time", serveTime)
                        .put("service_date", serveDate)
                        .put("service_mode", 1 + "")          //TODO
                        .put("number", createOrderGoodsNumber.getText().toString().trim())
                        .put("total_amount", bean.price + bean.visiting_fee + "")
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

    @OnClick({R.id.create_order_address_add, R.id.create_order_address_area, R.id.create_order_time_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_order_address_area:
                Intent intent = new Intent(getCtx(), OrderAddressActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.create_order_address_add:
                Intent intent1 = new Intent(getCtx(), AddressAddActivity.class);
                startActivityForResult(intent1, 201);
                break;
            case R.id.create_order_time_choose:
                chooseServeTime();
                break;
        }
    }

    private String serveTime;
    private String serveDate;

    private void chooseServeTime() {
        if (timeMap == null) showLoadingDialog();
        if (timeMap.size() == 0) return;
        ArrayList<String> firstData = new ArrayList<>();
        firstData.addAll(timeMap.keySet());
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                return firstData;
            }

            ArrayList<String> secondData;

            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
                secondData = new ArrayList<>();
                List<ServeTimeBean> timeBeans = timeMap.get(firstData.get(firstIndex));
                for (ServeTimeBean bean : timeBeans) {
                    if (bean.status == 0) {
                        secondData.add(bean.time + "(时间段已被选)");
                    } else {
                        secondData.add(bean.time);
                    }
                }
                return secondData;
            }

            @Nullable
            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }
        };
        LinkagePicker picker = new LinkagePicker(this, provider);
        picker.setCycleDisable(true);
        picker.setUseWeight(true);
//        picker.setLabel("小时制", "点");
//        picker.setSelectedIndex(0, 8);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                if (second.contains("被选")) {
                    ToastUtils.ToastMessage(getCtx(), "该时间段已被选");
                    return;
                }
                createOrderServeTime.setText(first + "  " + second);
                serveDate = first;
                serveTime = second;
            }
        });
        picker.show();
    }

    @NonNull
    private String getDataString(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        return calendar.get(Calendar.YEAR) + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
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


    private HashMap<String, List<ServeTimeBean>> timeMap;

    private void getServeTime(String service_id, String shop_id) {
        CarefreeRetrofit.getInstance().createApi(ServeApis.class).getAvailableServeTime(QueryMapBuilder.getIns().put("service_id", "1").put("shop_id", "114").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HashMap<String, List<ServeTimeBean>>>>() {
                    @Override
                    public void onSuccess(BaseResponse<HashMap<String, List<ServeTimeBean>>> hashMapBaseResponse) {
                        timeMap = hashMapBaseResponse.data;
                    }
                });
    }
}
