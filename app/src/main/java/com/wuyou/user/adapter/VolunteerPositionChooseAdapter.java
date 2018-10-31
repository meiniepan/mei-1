package com.wuyou.user.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.CommonUtil;

/**
 * Created by Solang on 2018/10/30.
 */

public class VolunteerPositionChooseAdapter extends BaseQuickAdapter<VolunteerProjectBean.PositionsBean, BaseHolder> {
    public VolunteerPositionChooseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder holder, VolunteerProjectBean.PositionsBean item) {
        holder.setText(R.id.tv_position_name, item.name);
        CheckBox checkBox = holder.getView(R.id.cb_position);
        checkBox.setChecked(item.isChosen);
    }

}
