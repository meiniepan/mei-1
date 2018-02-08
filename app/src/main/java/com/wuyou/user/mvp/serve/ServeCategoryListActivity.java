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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String serveId = getIntent().getStringExtra(Constant.CATEGORY_ID);
        serveList.setLayoutManager(new LinearLayoutManager(getCtx()));
        serveList.addItemDecoration(new RecycleViewDivider(
                getCtx(), LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(this,8), getResources().getColor(R.color.tint_bg)));
        adapter = new ServeListAdapter(getCtx(),R.layout.item_serve_list);
        serveList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getCtx(), ServeDetailActivity.class);
            ServeBean bean = (ServeBean) adapter.getData().get(position);
            intent.putExtra(Constant.SERVE_ID,bean.id);
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> mPresenter.getServeMore(), serveList);
        serveCategoryStatus.showProgressView();
        mPresenter.getServe(serveId);
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
        startActivity(intent);
    }

    @Override
    public void getServeSuccess(ServeListResponse list) {
        serveCategoryStatus.showContentView();
        adapter.setNewData(list.list);
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
}
