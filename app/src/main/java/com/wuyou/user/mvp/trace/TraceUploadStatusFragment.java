package com.wuyou.user.mvp.trace;

import android.os.Bundle;

import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/22.
 */

public class TraceUploadStatusFragment extends BaseFragment<TraceContract.View, TraceContract.Presenter> implements TraceContract.View {
    @BindView(R.id.rv_trace_upload_record)
    RefreshRecyclerView recyclerView;
    TraceRecordAdapter adapter;
    private int status = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_trace_upload_status;
    }

    @Override
    protected TraceContract.Presenter getPresenter() {
        return new TracePresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        status = getArguments().getInt("h");
        recyclerView.setRefreshAction(() -> mPresenter.getData(status));
        if (adapter == null) {
            adapter = new TraceRecordAdapter(R.layout.item_trace_record);
            recyclerView.setAdapter(adapter);
        }
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.item_btn_trace_confirm) {
                showLoadingDialog();
                mPresenter.approveAndExec(position, (TraceIPFSBean) adapter.getData().get(position));
            }
        });
        adapter.disableLoadMoreIfNotFullPage();
        if (status == 1) {
            recyclerView.showProgressView();
            mPresenter.getData(status);
        }

        CarefreeDaoSession.getInstance().getAllFinishedTraceRecord();
    }


    @Override
    protected void loadDataWhenVisible() {
        status = getArguments().getInt("h");
        if (adapter == null) {
            adapter = new TraceRecordAdapter(R.layout.item_trace_record);
            recyclerView.setAdapter(adapter);
        }
        if (status == 0 || status == 2) {
            recyclerView.showProgressView();
            mPresenter.getData(status);
        }
    }

    @Override
    public void getAuthDataSuccess(List<TraceIPFSBean> data) {
        if (data.size() == 0) {
            recyclerView.showEmptyView();
        } else {
            recyclerView.showContentView();
            adapter.setNewData(data);
        }
    }

    @Override
    public void approveAndExecSuccess(int position) {
        adapter.remove(position);
        recyclerView.showEmptyView();
    }

    @Override
    public void showError(String message, int res) {
        recyclerView.showErrorView(message);
    }
}
