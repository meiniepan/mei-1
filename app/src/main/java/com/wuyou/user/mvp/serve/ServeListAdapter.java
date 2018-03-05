package com.wuyou.user.mvp.serve;

import android.content.Context;
import android.widget.ImageView;

import com.wuyou.user.R;
import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServeListAdapter extends BaseQuickAdapter<ServeBean, BaseHolder> {
    Context context;

    public ServeListAdapter(Context ctx, int layoutResId) {
        super(layoutResId);
        context = ctx;
    }

    @Override
    protected void convert(BaseHolder helper, ServeBean item) {
        ImageView imageView = helper.getView(R.id.serve_item_picture);
        GlideUtils.loadImage(context, item.image, imageView);
        helper.setText(R.id.serve_item_name, item.name)
                .setText(R.id.serve_item_count, item.sell_quantity)
                .setText(R.id.serve_item_price, "￥" + item.price + "/小时")
                .setText(R.id.serve_item_point, item.high_praise_proportion)
                .setText(R.id.serve_item_rate_number, item.star+".0");
    }
}
