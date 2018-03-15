package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.bean.response.ServeListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServeCategoryListActivity extends BaseActivity<ServeContract.View, ServeContract.Presenter> implements ServeContract.View {
    @BindView(R.id.serve_category)
    TextView serveCategory;
    @BindView(R.id.serve_category_list)
    RecyclerView serveList;
    @BindView(R.id.serve_category_status)
    StatusLayout serveCategoryStatus;
    private ServeListAdapter adapter;
    private String categoryId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent i = getIntent();
        categoryId = i.getStringExtra(Constant.CATEGORY_ID);
        serveCategory.setText(i.getStringExtra(Constant.CATEGORY_NAME));
        serveList.setLayoutManager(new LinearLayoutManager(getCtx()));
        serveList.addItemDecoration(CommonUtil.getRecyclerDivider(this));
        adapter = new ServeListAdapter(getCtx(), R.layout.item_serve_list);
        serveList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getCtx(), ServeDetailActivity.class);
            ServeBean bean = (ServeBean) adapter.getData().get(position);
            intent.putExtra(Constant.SERVE_ID, bean.id);
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> mPresenter.getServeMore(), serveList);
        serveCategoryStatus.showProgressView();
        serveCategoryStatus.setErrorAction(v -> mPresenter.getServe(categoryId));
        mPresenter.getServe(categoryId);
    }

    @Override
    protected ServeContract.Presenter getPresenter() {
        return new ServePresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_category_list;
    }

    public void fastCreate(View view) {
        if (!checkUser(this)) return;
        Intent intent = new Intent(getCtx(), FastCreateActivity.class);
        intent.putExtra(Constant.CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    @Override
    public void getServeSuccess(ServeListResponse response) {
        serveCategoryStatus.showContentView();
        adapter.setNewData(response.list);
        if (adapter.getData().size() == 0) {
            serveCategoryStatus.showEmptyView("尚无当前种类服务");
            return;
        }
        if (response.has_more == 0) adapter.loadMoreEnd(true);
    }

    @Override
    public void loadMore(ServeListResponse data) {
        adapter.addData(data.list);
        if (data.has_more == 0) {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void loadMoreFail(String displayMessage, int code) {
        adapter.loadMoreFail();
    }

    @Override
    public void showError(String message, int res) {
        serveCategoryStatus.showErrorView(message);
    }
}
