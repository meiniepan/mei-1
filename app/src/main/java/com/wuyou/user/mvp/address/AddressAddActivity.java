package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.AddressBean;
import com.wuyou.user.data.remote.CityBean;
import com.wuyou.user.data.remote.response.AddressListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressAddActivity extends BaseActivity<AddressConstract.View, AddressConstract.Presenter> implements AddressConstract.View {
    @BindView(R.id.address_edit_title)
    TextView addressEditTitle;
    @BindView(R.id.address_edit_save)
    TextView addressEditSave;
    @BindView(R.id.address_edit_city)
    TextView addressEditCity;
    @BindView(R.id.address_edit_area)
    TextView addressEditArea;
    @BindView(R.id.address_edit_locate)
    ImageView addressEditLocate;
    @BindView(R.id.address_edit_detail)
    EditText addressEditDetail;
    @BindView(R.id.address_edit_receiver)
    EditText addressEditReceiver;
    @BindView(R.id.address_edit_phone)
    EditText addressEditPhone;
    @BindView(R.id.address_edit_delete)
    TextView addressEditDelete;
    private int flag;
    private double lat;
    private double lng;
    private String addressId;
    private String cityId;
    private String district;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra(Constant.ADDRESS_EDIT_FLAG, 0);
        if (flag == 0) { //新增
            addressEditTitle.setText(R.string.add_address);
            addressEditDelete.setVisibility(View.GONE);
        } else {//编辑
            addressEditTitle.setText(R.string.edit_address);
            AddressBean addressBean = getIntent().getParcelableExtra(Constant.ADDRESS_BEAN);
            setData(addressBean);
        }

        addressEditPhone.setText(CarefreeDaoSession.getInstance().getUserInfo().getMobile());

        CommonUtil.setEdDecimal(addressEditPhone, 5);
    }

    public void setData(AddressBean data) {
        addressId = data.id;
        cityId = data.city_id;
        district = data.district;
        addressEditCity.setText(data.city_name);
        addressEditArea.setText(data.district + data.area);
        addressEditDetail.setText(data.address);
        addressEditPhone.setText(data.mobile);
        addressEditReceiver.setText(data.name);
        lat = data.lat;
        lng = data.lng;
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_address;
    }


    @OnClick({R.id.address_edit_save, R.id.address_edit_locate, R.id.address_edit_delete, R.id.address_edit_city_click, R.id.address_edit_area})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.address_edit_save:
                if (addressEditCity.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "城市不能为空");
                    return;
                }
                if (addressEditArea.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "区域不能为空");
                    return;
                }
                if (addressEditDetail.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "详细地址不能为空");
                    return;
                }
                if (addressEditReceiver.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "联系人不能为空");
                    return;
                }
                if (!CommonUtil.checkPhone("", addressEditPhone.getText().toString().trim(), getCtx()))
                    return;
                AddressBean bean = new AddressBean();
                bean.city_name = addressEditCity.getText().toString().trim();
                bean.district = district;
                bean.area = addressEditArea.getText().toString().trim().split(district)[1];
                bean.address = addressEditDetail.getText().toString().trim();
                bean.name = addressEditReceiver.getText().toString().trim();
                bean.mobile = addressEditPhone.getText().toString().trim();
                bean.lat = lat;
                bean.lng = lng;
                showLoadingDialog("");
                if (flag == 0) {
                    if (CarefreeDaoSession.getInstance().getUserInfo().getAddress() == null) { //新增 第一个是默认地址
                        bean.is_default = 1;
                    }
                    mPresenter.addAddress(bean);
                } else {
                    mPresenter.updateAddress(addressId, bean);
                }
                break;
            case R.id.address_edit_locate:
                intent.setClass(getCtx(), AddressLocationActivity.class);
                intent.putExtra(Constant.ADDRESS_LOCATION_FLAG, 1);
                startActivityForResult(intent, 201);
                break;
            case R.id.address_edit_delete:
                showDeleteAlert();
                break;
            case R.id.address_edit_city_click:
                intent.setClass(getCtx(), CityChooseActivity.class);
                startActivityForResult(intent, 202);
                break;
            case R.id.address_edit_area:
                if (addressEditCity.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "请先选择城市");
                    return;
                }
                intent.setClass(getCtx(), AddressSearchActivity.class);
                startActivityForResult(intent, 203);
                break;
        }
    }

    private void showDeleteAlert() {
        new CustomAlertDialog.Builder(this).setTitle("提示").setMessage("确定要删除本条地址？")
                .setPositiveButton("确定", (dialog, which) ->
                        mPresenter.deleteAddress(0, addressId)).setNegativeButton("取消", null).create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200 && requestCode == 201) {
            PoiItem poiItem = data.getParcelableExtra(Constant.ADDRESS_RESULT);
            lat = poiItem.getLatLonPoint().getLatitude();
            lng = poiItem.getLatLonPoint().getLongitude();
            addressEditCity.setText(poiItem.getCityName());
            addressEditArea.setText(poiItem.getAdName() + poiItem.getTitle());
            district = poiItem.getAdName();
        } else if (resultCode == RESULT_OK && requestCode == 202) {
            CityBean cityBean = data.getParcelableExtra(Constant.CITY);
            cityId = cityBean.city_id;
            addressEditCity.setText(cityBean.city_name);
        } else if (resultCode == RESULT_OK && requestCode == 203) {
            PoiItem poiItem = data.getParcelableExtra(Constant.POI_RESULT);
            addressEditArea.setText(poiItem.getAdName() + poiItem.getTitle());
            district = poiItem.getAdName();
        }
    }

    @Override
    public void getAddressSuccess(AddressListResponse list) {

    }

    @Override
    public void updateSuccess(AddressBean data) {
        ToastUtils.ToastMessage(getCtx(), "地址编辑成功");
        Intent intent = new Intent();
        intent.putExtra(Constant.ADDRESS_BEAN, data);
        setResult(204, intent);
        finish();
    }

    @Override
    public void deleteSuccess(int position) {
        setResult(205);
        finish();
    }

    @Override
    public void addSuccess(AddressBean bean) {
        CarefreeDaoSession.getInstance().saveDefaultAddress(bean);
        ToastUtils.ToastMessage(getCtx(), "地址添加成功");
        Intent intent = new Intent();
        intent.putExtra(Constant.ADDRESS_BEAN, bean);
        setResult(203, intent);
        finish();
    }

    @Override
    protected AddressConstract.Presenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getCtx(), message);
    }
}
