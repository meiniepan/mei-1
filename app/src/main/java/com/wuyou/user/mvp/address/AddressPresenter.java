package com.wuyou.user.mvp.address;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.AddressId;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.AddressApis;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class AddressPresenter extends AddressConstract.Presenter {
    @Override
    void getAddress() {
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .getAddressList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AddressListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AddressListResponse> addressListResponseBaseResponse) {
                        mView.getAddressSuccess(addressListResponseBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getAddressMore(int type) {

    }

    @Override
    void deleteAddress(int position, String addressId) {
        CarefreeRetrofit.getInstance().createApi(AddressApis.class).deleteAddress(CarefreeDaoSession.getInstance().getUserId(), addressId, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        mView.deleteSuccess(position);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void updateAddress(String addressId, AddressBean addressBean) {
        addressBean.id = addressId;
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .updateAddress(CarefreeDaoSession.getInstance().getUserId(), addressId, QueryMapBuilder.getIns()
                        .put("city_id", addressBean.city_id)
                        .put("area", addressBean.area)
                        .put("address", addressBean.address)
                        .put("lat", addressBean.lat + "")
                        .put("lng", addressBean.lng + "")
                        .put("name", addressBean.name)
                        .put("mobile", addressBean.mobile)
                        .put("is_default", 0 + "")
                        .buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse addressIdBaseResponse) {
                        mView.updateSuccess(addressBean);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    public static Map getValue(Object thisObj) {
        Map map = new HashMap();
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get")) {
                    try {
                        Object value = m[i].invoke(thisObj);
                        if (value != null) {
                            String key = method.substring(3);
                            key = key.substring(0, 1).toUpperCase() + key.substring(1);
                            map.put(method, value);
                        }
                    } catch (Exception e) {
                        System.out.println("error:" + method);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    void addAddress(AddressBean addressBean) {
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .addAddress(CarefreeDaoSession.getInstance().getUserId(),
                        QueryMapBuilder.getIns().put("city_id", addressBean.city_id)
                                .put("area", addressBean.area)
                                .put("address", addressBean.address)
                                .put("lat", addressBean.lat + "")
                                .put("lng", addressBean.lng + "")
                                .put("name", addressBean.name)
                                .put("mobile", addressBean.mobile)
                                .buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AddressId>>() {
                    @Override
                    public void onSuccess(BaseResponse<AddressId> addressIdBaseResponse) {
                        addressBean.id = addressIdBaseResponse.data.address_id;
                        mView.addSuccess(addressBean);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }
}
