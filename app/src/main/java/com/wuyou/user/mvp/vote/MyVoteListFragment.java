package com.wuyou.user.mvp.vote;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.GsonBuilder;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosVoteListBean;
import com.wuyou.user.data.api.VoteRecord;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by DELL on 2018/10/15.
 */

public class MyVoteListFragment extends BaseFragment {
    @BindView(R.id.vote_my_record)
    CarefreeRecyclerView voteMyRecord;

    private HashMap<String, EosVoteListBean.RowsBean> votedData;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_my_vote;
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
        voteMyRecord.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(mCtx, 8));
        voteMyRecord.setRefreshAction(this::getVoteRecord);
    }

    @Override
    protected void fetchData() {
        recordAdapter = new VoteRecordAdapter();
        voteMyRecord.setAdapter(recordAdapter);
        getVoteRecord();
    }

    public void setVotedData(HashMap<String, EosVoteListBean.RowsBean> votedData) {
        this.votedData = votedData;
        if (recordAdapter != null) getVoteRecord();
    }

    public void getVoteRecord() {
        if (votedData == null) return;
        voteMyRecord.showProgressView();
        EoscDataManager.getIns().getTable(CarefreeDaoSession.getInstance().getMainAccount().getName(), Constant.ACTIVITY_CREATE_VOTE, "infos")
                .map(s -> {
                    VoteRecord listBean = new GsonBuilder().create().fromJson(s, VoteRecord.class);
                    ArrayList<EosVoteListBean.RowsBean> data = new ArrayList<>();
                    for (VoteRecord.RowsBean bean : listBean.rows) {
                        EosVoteListBean.RowsBean rowsBean = votedData.get(bean.voteid);
                        if (rowsBean != null) {
                            data.add(rowsBean);
                        }
                    }
                    return data;
                })
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<ArrayList<EosVoteListBean.RowsBean>>() {
            @Override
            public void onSuccess(ArrayList<EosVoteListBean.RowsBean> data) {
                voteMyRecord.setRefreshFinished();
                voteMyRecord.showContentView();
                if (data.size() == 0) {
                    voteMyRecord.showEmptyView();
                }
                recordAdapter.setNewData(data);
            }

            @Override
            protected void onFail(ApiException e) {
                voteMyRecord.showErrorView(e.getDisplayMessage());
            }
        });
    }

    private VoteRecordAdapter recordAdapter;

    class VoteRecordAdapter extends BaseQuickAdapter<EosVoteListBean.RowsBean, BaseHolder> {

        VoteRecordAdapter() {
            super(R.layout.item_vote_record);
        }

        @Override
        protected void convert(BaseHolder baseHolder, EosVoteListBean.RowsBean rowsBean) {
            baseHolder.setText(R.id.item_vote_record_title, rowsBean.title);
            GlideUtils.loadRoundCornerImage(mContext, Constant.IPFS_URL + rowsBean.logo, baseHolder.getView(R.id.item_vote_record_picture));
            baseHolder.getView(R.id.item_vote_record_statistic).setOnClickListener(v -> navigateToDetail(rowsBean));
        }

        private void navigateToDetail(EosVoteListBean.RowsBean rowsBean) {
            Intent intent = new Intent(mContext, VoteDetailActivity.class);
            intent.putExtra(Constant.HAS_VOTE, true);
            intent.putExtra(Constant.VOTE_ROW_BEAN, rowsBean);
            startActivity(intent);
        }
    }
}
