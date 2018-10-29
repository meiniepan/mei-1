package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.EosUtil;
import com.wuyou.user.util.glide.GlideUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class VolunteerProListAdapter extends BaseQuickAdapter<VolunteerProjectBean, BaseHolder> {
    public VolunteerProListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder holder, VolunteerProjectBean item) {
        int need = 0, participate = 0;
        for (VolunteerProjectBean.PositionsBean e : item.positions
                ) {
            need += e.amount;
            participate += e.enrolled.size();
        }
        holder.setText(R.id.tv_volunteer_pro_name, item.name)
                .setText(R.id.tv_volunteer_pro_need, need+"")
                .setText(R.id.tv_volunteer_pro_participate, participate+"");
        TextView isEnd = holder.getView(R.id.tv_volunteer_pro_is_end);
        if (EosUtil.isOverdue(item.enroll_end_time)){
            isEnd.setBackgroundResource(R.drawable.bac_gray_left_4_round);
            isEnd.setText("已结束");
        }else {
            isEnd.setBackgroundResource(R.drawable.bac_blue_left_4_round);
            isEnd.setText("招募中");
        }
        ImageView logo = holder.getView(R.id.iv_volunteer_pro_logo);
        GlideUtils.loadRoundCornerImage(mContext, Constant.IPFS_URL+item.logofile,logo);
    }

}
