package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VoteOption;
import com.wuyou.user.data.api.VoteOptionContent;
import com.wuyou.user.data.api.VoteQuestion;
import com.wuyou.user.mvp.vote.DoChooseListener;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class VoteQuestionAdapter extends BaseQuickAdapter<VoteQuestion, BaseHolder> implements DoChooseListener {
    private final boolean hasVote;
    private boolean isSingle;
    private VoteQuestionOptAdapter adapter;

    public VoteQuestionAdapter(int layoutResId, @Nullable List<VoteQuestion> data, boolean hasVote) {
        super(layoutResId, data);
        this.hasVote = hasVote;
    }

    @Override
    protected void convert(BaseHolder holder, VoteQuestion data) {
        holder.setText(R.id.tv_vote_detail_ques, data.question);
        if (data.single == 1) {
            holder.setText(R.id.tv_vote_detail_ques_style, "(单选)");
            isSingle = true;
        } else {
            holder.setText(R.id.tv_vote_detail_ques_style, "(多选)");
            isSingle = false;
        }
        initRv(holder, data);
    }

    private void initRv(BaseHolder holder, VoteQuestion data) {
        RecyclerView recyclerView = holder.getView(R.id.rv_vote_detail_opt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new VoteQuestionOptAdapter(R.layout.item_vote_detail_question_opt, data.option, this, isSingle, hasVote);
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(adapter);

    }

    @Override
    public void doChoose(VoteOptionContent data, boolean isSingle, List<VoteOptionContent> voteOptionContents) {
        if (isSingle) {
            data.isChecked = true;
            for (VoteOptionContent e : voteOptionContents
                    ) {
                if (data.id != e.id) {
                    e.isChecked = false;
                }
            }
            notifyDataSetChanged();
        }
    }
}
