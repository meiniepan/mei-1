package com.wuyou.user.mvp.address;

import android.text.TextUtils;

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
                        if (isAttach())mView.getAddressSuccess(addressListResponseBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach())mView.showError(e.getDisplayMessage(), e.getCode());
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
                .doOnNext(baseResponse -> {
                    if (CarefreeDaoSession.getInstance().getDefaultAddress() == null || TextUtils.equals(addressId, CarefreeDaoSession.getInstance().getDefaultAddress().id)) {
                        CarefreeDaoSession.getInstance().saveDefaultAddress(null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (isAttach()) mView.deleteSuccess(position);
                    }
                });
    }

    @Override
    void updateAddress(String addressId, AddressBean addressBean) {
        addressBean.id = addressId;
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .updateAddress(CarefreeDaoSession.getInstance().getUserId(), addressId, QueryMapBuilder.getIns().putObject(addressBean).buildPost())
                .subscribeOn(Schedulers.io())
                .doOnNext(baseResponse -> {
                    if (addressBean.is_default == 1)
                        CarefreeDaoSession.getInstance().saveDefaultAddress(addressBean);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse addressIdBaseResponse) {
                        if (isAttach())mView.updateSuccess(addressBean);
                    }
                });
    }

    @Override
    void addAddress(AddressBean addressBean) {
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .addAddress(CarefreeDaoSession.getInstance().getUserId(),
                        QueryMapBuilder.getIns().put("city_name", addressBean.city_name)
                                .put("area", addressBean.area)
                                .put("address", addressBean.address)
                                .put("lat", addressBean.lat + "")
                                .put("lng", addressBean.lng + "")
                                .put("name", addressBean.name)
                                .put("mobile", addressBean.mobile)
                                .put("district", addressBean.district)
                                .put("is_default", addressBean.is_default + "")
                                .buildPost())
                .subscribeOn(Schedulers.io())
                .doOnNext(addressIdBaseResponse -> {
                    if (CarefreeDaoSession.getInstance().getUserInfo().getAddress() == null) {
                        CarefreeDaoSession.getInstance().saveDefaultAddress(addressBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AddressId>>() {
                    @Override
                    public void onSuccess(BaseResponse<AddressId> addressIdBaseResponse) {
                        addressBean.id = addressIdBaseResponse.data.address_id;
                        if (isAttach()) mView.addSuccess(addressBean);
                    }
                });
    }
}
