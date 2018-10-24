package com.wuyou.user.mvp.trace;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.util.glide.GlideUtils;

import java.util.List;

/**
 * Created by Soalng on 2018/10/23.
 */

public class TraceRecordImgAdapter extends BaseQuickAdapter<String, BaseHolder> {

    public TraceRecordImgAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, String item) {
        ImageView img = helper.getView(R.id.iv_item_trace);
        GlideUtils.loadRoundCornerImage(mContext, item, img);
        ImageView delete = helper.getView(R.id.iv_item_delete);
        delete.setVisibility(View.GONE);

    }
}
