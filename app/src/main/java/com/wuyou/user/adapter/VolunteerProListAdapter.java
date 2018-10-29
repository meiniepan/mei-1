package com.wuyou.user.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class VolunteerProListAdapter extends BaseQuickAdapter<VolunteerProjectBean, BaseHolder> {
    public VolunteerProListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder holder, VolunteerProjectBean data) {
        holder.setText(R.id.tv_volunteer_pro_list, data.name);
    }

}
