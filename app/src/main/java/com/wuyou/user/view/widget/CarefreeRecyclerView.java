package com.wuyou.user.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.gs.buluo.common.R;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.widget.recyclerHelper.OnRefreshListener;
import com.wuyou.user.bean.response.BaseItemBean;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hjn on 2017/7/10.
 */

public class CarefreeRecyclerView extends FrameLayout {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private StatusLayout statusLayout;

    public CarefreeRecyclerView(Context context) {
        this(context, null);
    }

    public CarefreeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        Drawable errorDrawable;
        Drawable emptyDrawable;
        Drawable loginDrawable;
        try {
            errorDrawable = a.getDrawable(R.styleable.RefreshRecyclerView_refreshErrorDrawable);
            emptyDrawable = a.getDrawable(R.styleable.RefreshRecyclerView_refreshEmptyDrawable);
            loginDrawable = a.getDrawable(R.styleable.RefreshRecyclerView_refreshLoginDrawable);
        } finally {
            a.recycle();
        }
        statusLayout.getEmptyImageView().setImageDrawable(emptyDrawable);
        statusLayout.getErrorImageView().setImageDrawable(errorDrawable);
        statusLayout.getLoginImageView().setImageDrawable(loginDrawable);
    }

    public CarefreeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = inflate(context, R.layout.common_status_refresh_recycler, this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(CommonUtil.getRecyclerDivider(context));
        mSwipeRefreshLayout = view.findViewById(R.id.recycler_swipe);
        statusLayout = view.findViewById(R.id.recycler_status);
        mSwipeRefreshLayout.setEnabled(false);
        setSwipeRefreshColorsFromRes(R.color.common_custom_color, R.color.common_custom_color_shallow, R.color.common_night_blue);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    public void setRefreshAction(final OnRefreshListener action) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(() -> action.onAction());
    }

    //刷新完成先调
    public void setRefreshFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.clearData();
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void showEmptyView() {
        statusLayout.showEmptyView();
    }

    public void showEmptyView(String msg) {
        statusLayout.showEmptyView(msg);
    }

    public void showErrorView() {
        statusLayout.showErrorView();
    }

    public void showErrorView(String msg) {
        statusLayout.showErrorView(msg);
    }

    public void showContentView() {
        statusLayout.showContentView();
    }

    public StatusLayout getStatusLayout() {
        return statusLayout;
    }

    public void showProgressView() {
        statusLayout.showProgressView();
    }

    public String emptyMessage;
    public String startId;

    public <T extends BaseItemBean> void initData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        getData(firstObservable);
        statusLayout.setErrorAction(v -> getData(firstObservable));
    }

    private <T extends BaseItemBean> void getData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        mSwipeRefreshLayout.setRefreshing(false);
        statusLayout.showProgressView();
        firstObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<T>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<T>> listResponseBaseResponse) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        statusLayout.showContentView();
                        ListResponse<T> data = listResponseBaseResponse.data;
                        mAdapter.setNewData(data.list);
                        if (data.list == null || data.list.size() == 0) {
                            statusLayout.showEmptyView(emptyMessage);
                            return;
                        }
                        startId = data.list.get(data.list.size() - 1).id;
                        if (data.has_more == 0) {
                            mAdapter.loadMoreEnd(true);
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    public <T extends BaseItemBean> void getDataMore(Observable<BaseResponse<ListResponse<T>>> observable) {
        observable.compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<T>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<T>> listResponseBaseResponse) {
                        ListResponse<T> data = listResponseBaseResponse.data;
                        mAdapter.addData(data.list);
                        if (data.has_more == 0) {
                            mAdapter.loadMoreEnd(true);
                        }
                        startId = data.list.get(data.list.size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mAdapter.loadMoreFail();
                    }
                });
    }

    public void setLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener loadMoreListener) {
        mAdapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
    }

    public <T extends BaseItemBean> void activeRefresh(Observable<BaseResponse<ListResponse<T>>> observable) {
        setRefreshAction(() -> getData(observable));
    }
}
