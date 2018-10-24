package com.wuyou.user.mvp.trace;

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
 * Created by Soalng on 2018/10/23.
 */

public class TraceUploadImgAdapter extends BaseQuickAdapter<String, BaseHolder> {

    public TraceUploadImgAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, String item) {
        ImageView img = helper.getView(R.id.iv_item_trace);
        ImageView delete = helper.getView(R.id.iv_item_delete);
        if (item.equals("00")) {
            img.setImageResource(R.mipmap.icon_trace_upload);
            delete.setVisibility(View.GONE);
        } else {
            GlideUtils.loadImage(mContext, item, img);
            delete.setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.iv_item_delete);
    }
}
