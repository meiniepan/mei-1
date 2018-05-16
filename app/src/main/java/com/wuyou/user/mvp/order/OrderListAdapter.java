package com.wuyou.user.mvp.order;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.util.CommonUtil;
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
        helper.setText(R.id.order_item_status, CommonUtil.getOrderStatusString(item.status))
                .setText(R.id.order_item_title, item.service.service_name)
                .setText(R.id.order_item_spot_name, item.shop.shop_name)
                .setText(R.id.order_item_number, item.order_number)
                .setText(R.id.order_item_price, CommonUtil.formatPrice(item.amount));

        ImageView imageView = helper.getView(R.id.order_item_picture);
        GlideUtils.loadImage(mCtx, item.service.photo, imageView);

        TextView tvStatus = helper.getView(R.id.order_item_status);
        TextView tvAct = helper.getView(R.id.order_item_orange);
        TextView tvCancel = helper.getView(R.id.order_item_blue);
        switch (item.status) {
            case 1:
                tvStatus.setText(R.string.wait_pay);
                tvStatus.setTextColor(tvStatus.getResources().getColor(R.color.common_orange));
                tvCancel.setText(R.string.cancel_order);
                tvAct.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvStatus.setText(R.string.serving);
                tvStatus.setTextColor(tvStatus.getResources().getColor(R.color.common_orange));
                tvCancel.setText(R.string.ask_help);
                if (item.can_finish == 1) {
                    tvAct.setVisibility(View.VISIBLE);
                    if (item.second_payment == 0) {
                        tvAct.setText(R.string.finish);
                    } else {
                        tvAct.setText(R.string.go_pay);
                    }
                } else {
                    tvAct.setVisibility(View.GONE);
                }
                tvCancel.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvStatus.setText(R.string.wait_comment);
                tvStatus.setTextColor(tvStatus.getResources().getColor(R.color.common_green));
                tvCancel.setText(R.string.comment);
                tvCancel.setVisibility(View.VISIBLE);
                tvAct.setVisibility(View.GONE);
                break;
            case 4:
                tvStatus.setText(R.string.finished);
                tvStatus.setTextColor(tvStatus.getResources().getColor(R.color.gray_99));
                tvCancel.setText(R.string.ask_help);
                tvCancel.setVisibility(View.VISIBLE);
                tvAct.setVisibility(View.GONE);
                break;
            case 5:
                tvStatus.setText(R.string.canceled);
                tvStatus.setTextColor(tvStatus.getResources().getColor(R.color.gray_99));
                tvCancel.setVisibility(View.GONE);
                tvAct.setVisibility(View.GONE);
                break;
        }
        helper.addOnClickListener(R.id.order_item_orange);
        helper.addOnClickListener(R.id.order_item_blue);
        if (buttonGone) {
            helper.getView(R.id.home_gone_view).setVisibility(View.GONE);
        }
    }

    public void setButtonGone() {
        buttonGone = true;
    }
}
