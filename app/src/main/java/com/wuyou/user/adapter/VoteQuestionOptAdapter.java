package com.wuyou.user.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VoteOptionContent;
import com.wuyou.user.data.api.VoteQuestion;
import com.wuyou.user.mvp.vote.DoChooseListener;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class VoteQuestionOptAdapter extends BaseQuickAdapter<VoteOptionContent, BaseHolder> {
    private final DoChooseListener chooseListener;
    private final boolean isSingle;
    private final boolean hasVote;
    private final int voteSum;

    public VoteQuestionOptAdapter(int layoutResId, @Nullable List<VoteOptionContent> data, DoChooseListener chooseListener, boolean isSingle, boolean hasVote, int voteSum) {
        super(layoutResId, data);
        this.chooseListener = chooseListener;
        this.isSingle = isSingle;
        this.hasVote = hasVote;
        this.voteSum = voteSum;
    }

    @Override
    protected void convert(BaseHolder holder, VoteOptionContent data) {
        data.id = holder.getAdapterPosition();
        holder.setText(R.id.tv_vote_detail_opt, data.optioncontent);
        CheckBox checkBox = holder.getView(R.id.cb_vote_opt);
        LinearLayout linearLayout = holder.getView(R.id.ll_vote_opt);
        ProgressBar progressBar = holder.getView(R.id.pb_vote_opt);
        TextView tvScale = holder.getView(R.id.tv_vote_opt_scale);
        if (hasVote) {
            checkBox.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            progressBar.setProgress(data.number * 100 / voteSum);
            tvScale.setText((data.number * 100 / voteSum) + "%");
        } else {
            checkBox.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            checkBox.setChecked(data.isChecked);
            checkBox.setOnClickListener(v -> {
                data.isChecked = true;
                chooseListener.doChoose(data, isSingle, getData(), this);
            });
        }


    }


}
