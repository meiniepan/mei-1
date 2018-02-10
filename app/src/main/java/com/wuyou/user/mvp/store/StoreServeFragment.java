package com.wuyou.user.mvp.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.mvp.serve.ServeContract;
import com.wuyou.user.mvp.serve.ServeDetailActivity;
import com.wuyou.user.mvp.serve.ServeListAdapter;
import com.wuyou.user.mvp.serve.ServePresenter;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2018/2/10.
 */

public class StoreServeFragment extends BaseFragment<ServeContract.View,ServeContract.Presenter> {
    @BindView(R.id.store_serve_list)
    RecyclerView recyclerView;
    private ServeListAdapter adapter;
    private List<ServeBean> serveBeanList = new ArrayList<>();

    @Override
    public void showError(String message, int res) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store_serve;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        adapter = new ServeListAdapter(mCtx, R.layout.item_serve_list);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mCtx, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mCtx, 10), getResources().getColor(R.color.tint_bg)));
        recyclerView.setAdapter(adapter);
        adapter.setNewData(serveBeanList);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mCtx, ServeDetailActivity.class);
            ServeBean bean = (ServeBean) adapter.getData().get(position);
            intent.putExtra(Constant.SERVE_ID, bean.id);
            startActivity(intent);
        });
    }

    public void setData(List<ServeBean> data) {
        serveBeanList = data;
        if (adapter != null) adapter.setNewData(data);
    }

    @Override
    protected ServeContract.Presenter getPresenter() {
        return new ServePresenter();
    }
}
