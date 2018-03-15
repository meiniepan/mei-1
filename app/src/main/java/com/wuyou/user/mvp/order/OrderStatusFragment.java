package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.CommentActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.panel.PayPanel;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderStatusFragment extends BaseFragment<OrderContract.View, OrderContract.Presenter> implements OrderContract.View {
    @BindView(R.id.order_list)
    NewRefreshRecyclerView orderList;
    @BindView(R.id.order_list_layout)
    StatusLayout orderListLayout;
    private int type;
    private OrderListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_status_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        ArrayList<OrderBean> list = new ArrayList<>();
        adapter = new OrderListAdapter(mCtx,R.layout.item_order_list, list);
        orderList.setAdapter(adapter);
        orderList.setRefreshAction(this::refreshData);
        adapter.setOnItemChildClickListener((adapter, view, position) -> dealWithClick(position, (OrderBean) adapter.getData().get(position)));
        adapter.setOnItemClickListener((adapter, view, position) -> {
            OrderBean bean = (OrderBean) adapter.getData().get(position);
            Intent intent = new Intent(mCtx, OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, bean.id);
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> mPresenter.getOrderMore(type), orderList.getRecyclerView());
        orderListLayout.setEmptyAction(v -> {
            if (CarefreeDaoSession.getInstance().getUserId() == null) {
                Intent intent = new Intent(mCtx, LoginActivity.class);
                startActivity(intent);
            } else {
                mPresenter.getOrder(type);
            }
        });
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderListLayout.showEmptyView("请先登录");
            orderList.setRefreshEnable(false);
        } else {
            mPresenter.getOrder(type);
            orderList.setRefreshEnable(true);
        }
    }

    @Override
    public void fetchData() {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderListLayout.showEmptyView("请先登录");
            return;
        }
        orderListLayout.showProgressView();
        mPresenter.getOrder(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    private void refreshData() {
        orderListLayout.showProgressView();
        mPresenter.getOrder(type);
    }

    @Override
    public void showError(String message, int res) {
        if (res == 100) {
            ToastUtils.ToastMessage(mCtx, message);
        } else {
            orderListLayout.showErrorView(message);
        }
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    public void getOrderSuccess(OrderListResponse response) {
        orderListLayout.showContentView();
        orderList.setRefreshFinished();
        adapter.clearData();
        adapter.setNewData(response.list);
        if (adapter.getData().size() == 0) {
            orderListLayout.showEmptyView("当前尚无订单");
        }
        if (response.has_more == 0) adapter.loadMoreEnd(true);
    }

    @Override
    public void loadMore(OrderListResponse data) {
        adapter.addData(data.list);
        if (data.has_more == 0) {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void cancelSuccess(int pos) {
        adapter.remove(pos);
    }


    //详情接口回调，忽略
    @Override
    public void getOrderDetailSuccess(OrderBeanDetail bean) {
    }

    @Override
    public void loadMoreFail(String displayMessage, int code) {
        adapter.loadMoreFail();
    }

    @Override
    public void paySuccess() {
        refreshData();
    }

    @Override
    public void finishOrderSuccess() {
        refreshData();
    }

    private void dealWithClick(int position, OrderBean orderBean) {
        switch (orderBean.status) {
            case "待付款":
                PayPanel payPanel = new PayPanel(mCtx, new PayPanel.OnPayFinishListener() {
                    @Override
                    public void onPaySuccess() {
                        mPresenter.payOrder(orderBean.id);
                    }

                    @Override
                    public void onPayFail(ApiException e) {

                    }
                });
                payPanel.show();
                break;
            case "进行中":
                mPresenter.finishOrder(orderBean.id);
                break;
            case "待评价":
                Intent intent = new Intent(mCtx, CommentActivity.class);
                intent.putExtra(Constant.ORDER_BEAN,orderBean);
                startActivity(intent);
                break;
        }
    }
}
