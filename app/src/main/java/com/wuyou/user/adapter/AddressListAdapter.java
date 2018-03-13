package com.wuyou.user.adapter;

import android.widget.RadioButton;

import com.wuyou.user.R;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.ArrayList;

/**
 * Created by DELL on 2018/3/8.
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressBean, BaseHolder> {
    public AddressListAdapter(int layoutResId, ArrayList<AddressBean> data) {
        super(layoutResId, data);
    }

    public AddressListAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(BaseHolder helper, AddressBean item) {
        helper.setText(R.id.address_item_name, item.name)
                .setText(R.id.address_item_phone, item.mobile)
                .setText(R.id.address_item_detail, item.city_name + item.district + item.area + item.address);

        RadioButton radioButton = helper.getView(R.id.address_item_default);
        radioButton.setChecked(item.is_default == 1);
    }
}
