package com.wuyou.user.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.gs.buluo.common.R;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.widget.recyclerHelper.EaeRecyclerView;
import com.gs.buluo.common.widget.recyclerHelper.OnRefreshListener;
import com.gs.buluo.common.widget.recyclerHelper.refreshLayout.EasyRefreshLayout;
import com.wuyou.user.data.remote.response.BaseItemBean;
import com.wuyou.user.data.remote.response.ListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hjn on 2017/7/10.
 */

public class CarefreeRecyclerView extends FrameLayout {
    private EasyRefreshLayout mSwipeRefreshLayout;
    private EaeRecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    public CarefreeRecyclerView(Context context) {
        this(context, null);
    }

    public CarefreeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
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

        View view = inflate(context, R.layout.common_status_refresh_recycler, this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(CommonUtil.getRecyclerDivider(context));
        mSwipeRefreshLayout = view.findViewById(R.id.recycler_swipe);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setEnablePullToRefresh(false);
        setSwipeRefreshColorsFromRes(R.color.common_custom_color, R.color.common_custom_color_shallow, R.color.common_night_blue);
        mRecyclerView.setEmptyDrawable(emptyDrawable);
        mRecyclerView.setErrorDrawable(errorDrawable);
        mRecyclerView.setLoginDrawable(loginDrawable);
    }

    public CarefreeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EaeRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
//        mSwipeRefreshLayout.setColorSchemeResources(colors);
//        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    public void setRefreshAction(final OnRefreshListener action) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setEnablePullToRefresh(true);
        mSwipeRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            public void onRefreshing() {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        action.onAction();
                        mSwipeRefreshLayout.refreshComplete();
                    }
                }, 1000L);
            }
        });
    }

    //刷新完成先调
    public void setRefreshFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.clearData();
    }

    public EasyRefreshLayout getRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void showEmptyView() {
        mRecyclerView.showEmptyView();
    }

    public void showEmptyView(String msg) {
        mRecyclerView.showEmptyView(msg);
    }

    public void showErrorView() {
        mRecyclerView.showErrorView();
    }

    public void showErrorView(String msg) {
        mRecyclerView.showErrorView(msg);
    }

    public void showContentView() {
        mRecyclerView.showContentView();
    }

    public void showProgressView() {
        mRecyclerView.showProgressView();
    }

    public String emptyMessage;
    public String startId;

    //使用此处封装需要让 item bean对象继承 BaseItemBean--------------------------------------------------------------

    public <T extends BaseItemBean> void initData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        getData(firstObservable);
        mRecyclerView.setErrorAction(v -> getData(firstObservable));
    }

    public <T extends BaseItemBean> void initOrderData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        getOrderData(firstObservable);
        mRecyclerView.setErrorAction(v -> getOrderData(firstObservable));
    }

    private <T extends BaseItemBean> void getData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.showProgressView();
        firstObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<T>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<T>> listResponseBaseResponse) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.showContentView();
                        ListResponse<T> data = listResponseBaseResponse.data;
                        mAdapter.setNewData(data.list);
                        if (data.list == null || data.list.size() == 0) {
                            mRecyclerView.showEmptyView(emptyMessage);
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
                        mRecyclerView.showErrorView(e.getDisplayMessage());
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
                            return;
                        }
                        startId = data.list.get(data.list.size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mAdapter.loadMoreFail();
                    }
                });
    }

    private <T extends BaseItemBean> void getOrderData(Observable<BaseResponse<ListResponse<T>>> firstObservable) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.showProgressView();
        firstObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<T>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<T>> listResponseBaseResponse) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.showContentView();
                        ListResponse<T> data = listResponseBaseResponse.data;
                        mAdapter.setNewData(data.list);
                        if (data.list == null || data.list.size() == 0) {
                            mRecyclerView.showEmptyView(emptyMessage);
                            return;
                        }
                        startId = data.list.get(data.list.size() - 1).order_id;
                        if (data.has_more == 0) {
                            mAdapter.loadMoreEnd(true);
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    public <T extends BaseItemBean> void getOrderDataMore(Observable<BaseResponse<ListResponse<T>>> observable) {
        observable.compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<T>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<T>> listResponseBaseResponse) {
                        ListResponse<T> data = listResponseBaseResponse.data;
                        mAdapter.addData(data.list);
                        if (data.has_more == 0) {
                            mAdapter.loadMoreEnd(true);
                            return;
                        }
                        startId = data.list.get(data.list.size() - 1).order_id;
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

    public void setEmptyIcon(int emptyIcon) {
        mRecyclerView.setEmptyDrawable(getResources().getDrawable(emptyIcon));
    }
}
