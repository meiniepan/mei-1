package com.wuyou.user.mvp.order;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderBean, BaseHolder> {

    private boolean buttonGone =false;

    public OrderListAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, OrderBean item) {
        helper.setText(R.id.order_item_address, item.address)
                .setText(R.id.order_item_date, TribeDateUtils.dateFormat(new Date(item.created_at * 1000)))
                .setText(R.id.order_item_status, item.status)
                .setText(R.id.order_item_title, item.category_name)
                .setText(R.id.order_item_time, item.time_interval);

        TextView tvStatus = helper.getView(R.id.order_item_status);
        TextView tvAct = helper.getView(R.id.order_item_action);
        switch (item.status) {
            case "待付款":
                tvStatus.setText(R.string.wait_pay);
                tvStatus.setTextColor(0xffff904b);
                tvAct.setText(R.string.pay);
                tvAct.setTextColor(0xffffffff);
                tvAct.setBackgroundResource(R.drawable.orange_bg);
                break;
            case "进行中":
                tvStatus.setText(R.string.serving);
                tvStatus.setTextColor(0xff627db9);
                tvAct.setText(R.string.finish);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
            case "已完成":
                tvStatus.setText(R.string.finished);
                tvStatus.setTextColor(0xff627db9);
                tvAct.setVisibility(View.GONE);
//                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
            case "待评价":
                tvStatus.setText(R.string.finished);
                tvStatus.setTextColor(0xffff904b);
                tvAct.setText(R.string.comment);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
        }
        helper.addOnClickListener(R.id.order_item_action);
        if (buttonGone){
            tvAct.setVisibility(View.GONE);
        }
    }

    public void setButtonGone() {
        buttonGone = true;
    }
}
