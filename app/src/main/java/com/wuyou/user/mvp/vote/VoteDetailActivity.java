package com.wuyou.user.mvp.vote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VoteQuestionAdapter;
import com.wuyou.user.data.api.EosVoteListBean;
import com.wuyou.user.data.api.VoteOption;
import com.wuyou.user.data.api.VoteOptionContent;
import com.wuyou.user.data.api.VoteQuestion;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/15.
 */

public class VoteDetailActivity extends BaseActivity {
    @BindView(R.id.iv_vote_detail_bac)
    ImageView ivVoteDetailBac;
    @BindView(R.id.tv_vote_detail_deadline)
    TextView tvVoteDetailDeadline;
    @BindView(R.id.tv_vote_detail_people_num)
    TextView tvVoteDetailPeopleNum;
    @BindView(R.id.tv_vote_detail_title)
    TextView tvTitle;
    @BindView(R.id.tv_vote_detail_intro)
    TextView tvVoteDetailIntro;
    @BindView(R.id.rv_vote_detail)
    RecyclerView rvVoteDetail;
    @BindView(R.id.tv_vote_detail_community_name)
    TextView tvVoteDetailCommunityName;

    EosVoteListBean.RowsBean rowsBean;
    VoteQuestionAdapter adapter;
    boolean hasVote;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_vote_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.vote_detail));
        setTitleIcon(R.mipmap.icon_share, v -> ToastUtils.ToastMessage(getCtx(), R.string.no_function));
        initData();
    }

    private void initData() {
        rowsBean = getIntent().getParcelableExtra(Constant.VOTE_ROW_BEAN);
        hasVote = getIntent().getBooleanExtra(Constant.HAS_VOTE, false);
        GlideUtils.loadImage(getCtx(), Constant.IPFS_URL + rowsBean.logo, ivVoteDetailBac);
        tvTitle.setText(rowsBean.title);
        tvVoteDetailDeadline.setText(rowsBean.end_time);
        String peopleNum;
        if (rowsBean.voters.size() > 999) {
            peopleNum = "999+";
        } else {
            peopleNum = rowsBean.voters.size() + "";
        }
        tvVoteDetailPeopleNum.setText(peopleNum);
        tvVoteDetailCommunityName.setText(rowsBean.creator);
        tvVoteDetailIntro.setText(rowsBean.description);
        initRv();
    }

    private void initRv() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getCtx());
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rvVoteDetail.setLayoutManager(mLinearLayoutManager);
        rvVoteDetail.setHasFixedSize(true);
        rvVoteDetail.setNestedScrollingEnabled(false);
        adapter = new VoteQuestionAdapter(R.layout.item_vote_detail_question, rowsBean.contents, hasVote, rowsBean.voters.size());
        rvVoteDetail.setAdapter(adapter);
    }


    @OnClick(R.id.tv_vote_detail_confirm)
    public void onViewClicked() {
        if (hasVote) {
            finish();
        } else {
            ArrayList<VoteOption> list = new ArrayList<>();
            for (VoteQuestion e1 : rowsBean.contents) {
                List<Integer> chosenData = new ArrayList<>();
                for (VoteOptionContent e2 : e1.option) {
                    if (e2.isChecked) {
                        chosenData.add(e2.id);
                    }
                }
                if (chosenData.size() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "还有题目未完成！");
                    return;
                }
                VoteOption voteOption = new VoteOption(chosenData);
                list.add(voteOption);
            }
            Intent intent = new Intent(getCtx(), VotePledgeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.VOTE_OPT_LIST, list);
            intent.putExtras(bundle);
            intent.putExtra(Constant.VOTE_ID, rowsBean.id);
            startActivity(intent);
            finish();
        }
    }
}
