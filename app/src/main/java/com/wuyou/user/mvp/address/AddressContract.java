package com.wuyou.user.mvp.address;

import com.wuyou.user.data.remote.AddressBean;
import com.wuyou.user.data.remote.response.AddressListResponse;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by hjn on 2018/3/8.
 */

public interface AddressContract {
    interface View extends IBaseView {
        void getAddressSuccess(AddressListResponse list);

        void updateSuccess(AddressBean data);

        void deleteSuccess(int position);

        void addSuccess(AddressBean bean);

    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getAddress();

        abstract void getAddressMore(int type);

        abstract void deleteAddress(int position, String addressId);

        abstract void updateAddress(String addressId, AddressBean bean);

        abstract void addAddress(AddressBean orderId);
    }
}
