package com.wuyou.user.adapter;

import com.wuyou.user.R;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

/**
 * Created by DELL on 2018/3/8.
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressBean, BaseHolder> {
    public AddressListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder helper, AddressBean item) {
        helper.setText(R.id.address_item_name, item.name)
                .setText(R.id.address_item_phone, item.mobile)
                .setText(R.id.address_item_detail, item.city_name + item.district + item.area + item.address);
    }
}
