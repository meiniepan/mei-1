package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.wuyou.user.R;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressLocationListAdapter extends BaseQuickAdapter<PoiItem, BaseHolder> {
    public AddressLocationListAdapter(int layoutResId, @Nullable List<PoiItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, PoiItem item) {
        helper.setText(R.id.address_location_detail, item.getTitle())
                .setText(R.id.address_location_district, item.getSnippet() + item.getCityName() + item.getAdName());

        if (getData().indexOf(item) == 0) {
            helper.getView(R.id.address_location_mark).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.address_location_mark).setVisibility(View.GONE);
        }
    }
}
