package com.wuyou.user.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.EosUtil;
import com.wuyou.user.util.glide.GlideUtils;

/**
 * Created by Solang on 2018/10/30.
 */

public class VolunteerPositionListAdapter extends BaseQuickAdapter<VolunteerProjectBean.PositionsBean, BaseHolder> {
    public VolunteerPositionListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder holder, VolunteerProjectBean.PositionsBean item) {
        holder.setText(R.id.tv_volunteer_detail_position_name, item.name)
                .setText(R.id.tv_volunteer_detail_position_need, item.amount+"")
                .setText(R.id.tv_volunteer_detail_position_reward, CommonUtil.formatPrice(Float.parseFloat(item.rewards.split(" ")[0])) + "小时币");
    }

}
