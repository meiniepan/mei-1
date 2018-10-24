package com.wuyou.user.mvp.trace;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.TraceRecordEntity;
import com.wuyou.user.data.api.VoteOptionContent;
import com.wuyou.user.data.api.VoteQuestion;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.mvp.trace.TraceUploadImgAdapter;
import com.wuyou.user.mvp.vote.DoChooseListener;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class TraceRecordAdapter extends BaseQuickAdapter<TraceIPFSBean, BaseHolder> {


    public TraceRecordAdapter(int layoutResId, @Nullable List<TraceIPFSBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder holder, TraceIPFSBean item) {
        holder.setText(R.id.item_tv_trace_spec,item.content);
        initRv(holder, item);
        TextView tvStatus = holder.getView(R.id.item_tv_trace_status);
        TextView tvConfirm = holder.getView(R.id.item_btn_trace_confirm);
    }

    private void initRv(BaseHolder holder, TraceIPFSBean item) {
        RecyclerView recyclerView = holder.getView(R.id.item_rv_trace);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        TraceRecordImgAdapter adapter = new TraceRecordImgAdapter(R.layout.item_trace_auth_img, item.getPicture());
        recyclerView.setAdapter(adapter);
    }

}
