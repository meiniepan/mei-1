package com.wuyou.user.mvp.order;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderBean, BaseHolder> {

    private boolean buttonGone = false;
    private Context mCtx;

    public OrderListAdapter(Context context, int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
        mCtx = context;
    }

    @Override
    protected void convert(BaseHolder helper, OrderBean item) {
        helper.setText(R.id.order_item_status, item.status)
                .setText(R.id.order_item_title, item.service.service_name)
                .setText(R.id.order_item_spot_name, item.shop.shop_name)
                .setText(R.id.order_item_number, item.order_number)
                .setText(R.id.order_item_price, item.amount + "");

        ImageView imageView = helper.getView(R.id.order_item_picture);
        GlideUtils.loadImage(mCtx, item.service.photo, imageView);

        TextView tvStatus = helper.getView(R.id.order_item_status);
        TextView tvAct = helper.getView(R.id.order_item_orange);
        TextView tvCancel = helper.getView(R.id.order_item_blue);
        switch (item.status) {
            case "待付款":
                tvStatus.setText(R.string.wait_pay);
                tvCancel.setText(R.string.cancel_order);
                tvAct.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                break;
            case "进行中":
                tvStatus.setText(R.string.serving);
                tvCancel.setText(R.string.ask_help);
                if (item.can_finish == 1) {
                    tvAct.setVisibility(View.VISIBLE);
                    tvAct.setText(R.string.finish);
                } else {
                    tvAct.setVisibility(View.GONE);
                }
                tvCancel.setVisibility(View.VISIBLE);
                break;
            case "已完成":
                tvStatus.setText(R.string.finished);
                tvCancel.setVisibility(View.GONE);
                tvAct.setVisibility(View.GONE);
                break;
            case "待评价":
                tvStatus.setText(R.string.wait_comment);
                tvCancel.setText(R.string.comment);
                tvCancel.setVisibility(View.VISIBLE);
                tvAct.setVisibility(View.GONE);
                break;
        }
        helper.addOnClickListener(R.id.order_item_orange);
        helper.addOnClickListener(R.id.order_item_blue);
        if (buttonGone) {
            tvAct.setVisibility(View.GONE);
        }
    }

    public void setButtonGone() {
        buttonGone = true;
    }
}
