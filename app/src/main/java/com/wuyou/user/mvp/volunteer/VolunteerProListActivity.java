package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;

import com.google.gson.GsonBuilder;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VolunteerProListAdapter;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosVoteListBean;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.EosUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Function;

/**
 * Created by Solang on 2018/10/26.
 */

public class VolunteerProListActivity extends BaseActivity {
    @BindView(R.id.rv_volunteer)
    RefreshRecyclerView recyclerView;
    VolunteerProListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_volunteer_list;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new VolunteerProListAdapter(R.layout.item_volunteer_list);
        recyclerView.setAdapter(adapter);
        getVolunteerProList();
    }

    public void getVolunteerProList() {
        recyclerView.showProgressView();
        EoscDataManager.getIns().getTable("samkunnbanb1", Constant.ACTIVITY_TIME_BANK, "task")
                .map((Function<String, List<VolunteerProjectBean.RowsBean>>) (String s) -> {
                    VolunteerProjectBean listBean = new GsonBuilder().create().fromJson(s, VolunteerProjectBean.class);
                    ArrayList<VolunteerProjectBean.RowsBean> list = new ArrayList<>();
                    for (VolunteerProjectBean.RowsBean rowsBean : listBean.rows) {
                            list.add(rowsBean);
                    }
                    return list;
                })
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<List<VolunteerProjectBean.RowsBean>>() {
            @Override
            public void onSuccess(List<VolunteerProjectBean.RowsBean> data) {
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
