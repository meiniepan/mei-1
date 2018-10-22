package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.TraceRecordEntity;
import com.wuyou.user.data.api.VoteOptionContent;
import com.wuyou.user.data.api.VoteQuestion;
import com.wuyou.user.mvp.vote.DoChooseListener;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class TraceRecordAdapter extends BaseQuickAdapter<TraceRecordEntity, BaseHolder> {


    public TraceRecordAdapter(int layoutResId, @Nullable List<TraceRecordEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder holder, TraceRecordEntity item) {
        //todo

    }

}
