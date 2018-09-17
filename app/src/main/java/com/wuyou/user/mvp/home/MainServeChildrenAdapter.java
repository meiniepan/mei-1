package com.wuyou.user.mvp.home;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.response.CategoryChild;
import com.wuyou.user.util.glide.GlideUtils;

import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class MainServeChildrenAdapter extends BaseQuickAdapter<CategoryChild, BaseHolder> {

    public MainServeChildrenAdapter(int layoutResId, @Nullable List<CategoryChild> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, CategoryChild item) {
        if (helper.getPosition() / 2 == 0) {
            helper.getView(R.id.space_head).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.space_head).setVisibility(View.VISIBLE);
        }
        TextView textView = helper.getView(R.id.main_serve_name);
        textView.setText(item.name);

//        int padding = textView.getPaddingStart();
        ImageView imageView = helper.getView(R.id.iv_serve_child);
        if (item.icon == null) {
            item.icon = "";
        }
        if (item.position % 3 == 0) {
            GlideUtils.loadImageWithHolder(mContext, item.icon, imageView, R.mipmap.icon_default_orange);
        } else if (item.position % 3 == 1) {
            GlideUtils.loadImageWithHolder(mContext, item.icon, imageView, R.mipmap.icon_default_green);
        } else {
            GlideUtils.loadImageWithHolder(mContext, item.icon, imageView, R.mipmap.icon_default_blue);
        }
//        textView.setPadding(padding, padding, padding, padding);
    }
}
