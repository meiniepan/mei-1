package com.wuyou.user.mvp.serve;

import android.content.Context;
import android.widget.ImageView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.util.glide.GlideUtils;

import io.techery.properratingbar.ProperRatingBar;

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
        GlideUtils.loadRoundCornerImage(context, item.image, imageView);
        helper.setText(R.id.serve_item_name, item.service_name)
                .setText(R.id.serve_item_count, item.sold)
                .setText(R.id.serve_item_price, "￥" + item.price + "/" + item.unit)
                .setText(R.id.serve_item_point, item.high_praise)
                .setText(R.id.serve_item_rate_number, item.star / 2 + ".0")
                .setText(R.id.serve_item_store, item.shop_name);

        ProperRatingBar ratingBar = helper.getView(R.id.serve_item_rate);
        ratingBar.setRating(item.star / 2);
    }
}
