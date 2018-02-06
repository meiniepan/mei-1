package com.wuyou.user.mvp.order;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

class OrderListAdapter extends BaseQuickAdapter<OrderBean, BaseHolder> {
    public OrderListAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, OrderBean item) {
        helper.setText(R.id.order_item_address, item.address)
//                .setText(R.id.order_item_date, TribeDateUtils.dateFormat6(item.created_at))
//                .setText(R.id.order_item_status, item.status)
                .setText(R.id.order_item_title, item.category_name);

        TextView tvStatus = helper.getView(R.id.order_item_status);
        TextView tvAct = helper.getView(R.id.order_item_action);
        switch (item.status) {
            case "待确认":
                tvStatus.setText(R.string.order_accepted);
                tvStatus.setTextColor(0xff627db9);
                tvAct.setText(R.string.cancel_order);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
            case "待服务":
                tvStatus.setText(R.string.delivered);
                tvStatus.setTextColor(0xff627db9);
                tvAct.setText(R.string.cancel_order);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
            case "待支付":
                tvStatus.setText(R.string.wait_pay);
                tvStatus.setTextColor(0xffff904b);
                tvAct.setText(R.string.pay);
                tvAct.setTextColor(0xffffffff);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
            case "待评价":
                tvStatus.setText(R.string.finished);
                tvStatus.setTextColor(0xffff904b);
                tvAct.setText(R.string.comment);
                tvAct.setBackgroundResource(R.drawable.cancel_order_background);
                break;
        }
        helper.addOnClickListener(R.id.order_item_action);
    }
}
