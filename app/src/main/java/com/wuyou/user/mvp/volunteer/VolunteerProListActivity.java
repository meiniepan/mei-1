package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VolunteerProListAdapter;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.ListRowResponse;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/26.
 */

public class VolunteerProListActivity extends BaseActivity {
    @BindView(R.id.rv_volunteer)
    RefreshRecyclerView recyclerView;
    VolunteerProListAdapter adapter;
    List<VolunteerProjectBean> data;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_volunteer_list;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.volunteer_project));
        initRv();
        getVolunteerProList();
    }

    private void initRv() {
        adapter = new VolunteerProListAdapter(R.layout.item_volunteer_project);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Intent intent = new Intent(getCtx(), VolunteerProDetailActivity.class);
            intent.putExtra(Constant.VOLUNTEER_PROJECT, data.get(i));
            startActivity(intent);
        });
    }

    public void getVolunteerProList() {
        recyclerView.showProgressView();
        EoscDataManager.getIns().getTable("samkunnbanb1", Constant.ACTIVITY_TIME_BANK, "task")
                .map((String s) -> {
                    ListRowResponse<VolunteerProjectBean> listBean = new Gson().fromJson(s, new TypeToken<ListRowResponse<VolunteerProjectBean>>() {
                    }.getType());
                    data = listBean.rows;

                    return data;
                })
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<List<VolunteerProjectBean>>() {
            @Override
            public void onSuccess(List<VolunteerProjectBean> data) {
                recyclerView.showContentView();
                adapter.setNewData(data);
                if (data.size() == 0) {
                    recyclerView.showEmptyView("当前暂无志愿项目");
                }
            }

            @Override
            protected void onFail(ApiException e) {
                recyclerView.showErrorView(e.getDisplayMessage());
            }
        });
    }
}
