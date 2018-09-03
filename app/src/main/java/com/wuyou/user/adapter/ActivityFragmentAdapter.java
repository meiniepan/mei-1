package com.wuyou.user.adapter;

import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.ActivityListBean;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideUtils;

/**
 * Created by DELL on 2018/8/13.
 */

public class ActivityFragmentAdapter extends BaseQuickAdapter<ActivityListBean, BaseHolder> {
    public ActivityFragmentAdapter() {
        super(R.layout.item_fragment_activity);
    }

    @Override
    protected void convert(BaseHolder baseHolder, ActivityListBean activityBean) {
        baseHolder.setText(R.id.activity_title, activityBean.title)
                .setText(R.id.activity_address, activityBean.address)
                .setText(R.id.activity_date, activityBean.date)
                .setText(R.id.activity_left_person, "剩余" + activityBean.surplus + "位");

        GlideUtils.loadImage(mContext, activityBean.image[0], baseHolder.getView(R.id.activity_picture));

        TextView feeView = baseHolder.getView(R.id.activity_fee);
        if (activityBean.free == 1) {
            feeView.setText("免费");
            feeView.setTextColor(mContext.getResources().getColor(R.color.common_dark));
        } else {
            feeView.setTextColor(mContext.getResources().getColor(R.color.common_red));
            setFeeRange(feeView, activityBean);
        }
        TextView view = baseHolder.getView(R.id.activity_status);
        if (activityBean.status == 0) {
            view.setVisibility(View.GONE);
        } else if (activityBean.status == 1) {
            view.setText("已报满");
            view.setBackgroundResource(R.drawable.activity_full);
            view.setVisibility(View.VISIBLE);
        } else if (activityBean.status == 2) {
            view.setText("已结束");
            view.setBackgroundResource(R.drawable.activity_cancel);
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setFeeRange(TextView feeView, ActivityListBean activityBean) {
        if (activityBean.fee == null || activityBean.fee.size() == 0) return;
        float minPrice = activityBean.fee.get(0).price;
        float maxPrice = activityBean.fee.get(0).price;
        for (ActivityListBean.ActivityFee fee : activityBean.fee) {
            if (fee.price < minPrice) {
                minPrice = fee.price;
            }
            if (fee.price > maxPrice) {
                maxPrice = fee.price;
            }
        }
        if (minPrice == maxPrice) {
            feeView.setText(String.format("¥%s", CommonUtil.formatPrice(minPrice)));
        } else {
            feeView.setText(String.format("¥%s～%s", CommonUtil.formatPrice(minPrice), CommonUtil.formatPrice(maxPrice)));
        }
    }
}
