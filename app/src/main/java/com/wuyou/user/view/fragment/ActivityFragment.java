package com.wuyou.user.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ActivityFragmentAdapter;
import com.wuyou.user.bean.ActivityListBean;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.WebActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import butterknife.BindView;

/**
 * Created by DELL on 2018/8/13.
 */

public class ActivityFragment extends BaseFragment {
    @BindView(R.id.activity_recycler)
    CarefreeRecyclerView activityRecyclerView;
    private ActivityFragmentAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_activity;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new ActivityFragmentAdapter();
        activityRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this::getDataMore, activityRecyclerView.getRecyclerView());
        activityRecyclerView.setRefreshAction(this::getData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String activityId = adapter.getData().get(i).id;
                navigateToWeb(activityId);
            }
        });
    }

    private void navigateToWeb(String activityId) {
        Intent intent = new Intent();
        if (CommonUtil.checkNetworkNoConnected(mCtx)) return;
        if (TextUtils.isEmpty(Constant.WEB_URL)) return;
        intent.setClass(mCtx, WebActivity.class);
        if (CarefreeDaoSession.getInstance().getUserInfo() == null) {
            intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL);
        } else {
            intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL + "activity_detail?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken() + "&activity_id=" + activityId);
        }
        startActivity(intent);
    }

    String sort = "0";

    @Override
    protected void fetchData() {
        getData();
    }

    private void getData() {
        activityRecyclerView.showProgressView();
        activityRecyclerView.setRefreshFinished();
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getActivityList(QueryMapBuilder.getIns().put("flag", "1").put("sort", sort).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ActivityListBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ActivityListBean>> listResponseBaseResponse) {
                        activityRecyclerView.showContentView();
                        ListResponse<ActivityListBean> data = listResponseBaseResponse.data;
                        if (data.list.size() == 0) {
                            activityRecyclerView.showEmptyView(getString(R.string.no_activity));
                            return;
                        }
                        adapter.setNewData(data.list);
                        sort = data.list.get(data.list.size() - 1).sort;
                        if (data.has_more == 0) {
                            adapter.loadMoreEnd(true);
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        activityRecyclerView.showErrorView(e.getDisplayMessage());
                    }
                });
    }


    public void getDataMore() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getActivityList(QueryMapBuilder.getIns().put("flag", "2").put("sort", sort).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ActivityListBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ActivityListBean>> listResponseBaseResponse) {
                        ListResponse<ActivityListBean> data = listResponseBaseResponse.data;
                        adapter.addData(data.list);
                        sort = data.list.get(data.list.size() - 1).sort;
                        if (data.has_more == 0) {
                            adapter.loadMoreEnd();
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        adapter.loadMoreFail();
                    }
                });
    }
}
