package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.text.style.TtsSpan;

import com.wuyou.user.R;
import com.wuyou.user.bean.ServeSites;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by DELL on 2018/3/12.
 */

public class ServeSitesAdapter extends BaseQuickAdapter<ServeSites, BaseHolder> {
    public ServeSitesAdapter(int layoutResId, @Nullable List<ServeSites> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServeSites item) {
        helper.setText(R.id.site_item_name, item.name)
                .setText(R.id.site_item_address, item.city_name + item.community_name + item.address);
        if (item.distance > 1000) {
            helper.setText(R.id.site_item_distance, new DecimalFormat("#.00").format(item.distance / 1000.0f) + "km");
        } else {
            helper.setText(R.id.site_item_distance, new DecimalFormat("#.00").format(item.distance) + "m");
        }
    }
}
