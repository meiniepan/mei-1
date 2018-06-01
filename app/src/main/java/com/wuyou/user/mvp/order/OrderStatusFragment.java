package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.event.OrderEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.CommentActivity;
import com.wuyou.user.view.activity.HelpRobotActivity;
import com.wuyou.user.view.fragment.BaseFragment;

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
    RefreshRecyclerView orderList;
    private int type;
    private OrderListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_status_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        setUpStatus();
        ArrayList<OrderBean> list = new ArrayList<>();
        adapter = new OrderListAdapter(mCtx, R.layout.item_order_list, list);
        orderList.setAdapter(adapter);
        orderList.setRefreshAction(this::refreshData);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.order_item_orange) {
                dealWithOrangeButtonClick(position, (OrderBean) adapter.getData().get(position));
            } else {
                dealWithBlueButtonClick(position, (OrderBean) adapter.getData().get(position));
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            OrderBean bean = (OrderBean) adapter.getData().get(position);
            Intent intent = new Intent(mCtx, OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, bean.order_id);
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> mPresenter.getOrderMore(type), orderList.getRecyclerView());
        adapter.disableLoadMoreIfNotFullPage();
    }

    public void setUpStatus() {
        orderList.getStatusLayout().getEmptyActView().setVisibility(View.GONE);
        orderList.getStatusLayout().setLoginAction(v -> {
            Intent intent = new Intent(mCtx, LoginActivity.class);
            startActivity(intent);
        });
        orderList.getStatusLayout().getLoginActView().setText(R.string.login_now);
        orderList.getStatusLayout().setErrorAction(v -> {
            orderList.getStatusLayout().showProgressView();
            mPresenter.getOrder(type);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderList.getStatusLayout().showLoginView(getString(R.string.no_login));
            orderList.getStatusLayout().setEnabled(false);
        } else {
            orderList.getStatusLayout().showProgressView();
            mPresenter.getOrder(type);
            orderList.getRefreshLayout().setEnabled(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderChagedEvent(OrderEvent event) {
        refreshData();
    }

    @Override
    public void fetchData() {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderList.getStatusLayout().showLoginView(getString(R.string.no_login));
            return;
        }
        orderList.getStatusLayout().showProgressView();
        mPresenter.getOrder(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    private void refreshData() {
        orderList.getStatusLayout().showProgressView();
        mPresenter.getOrder(type);
    }

    @Override
    public void showError(String message, int res) {
        orderList.getRefreshLayout().setRefreshing(false);
        if (res == 100) {
            ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
        } else {
            orderList.getStatusLayout().showErrorView(getString(R.string.connect_fail));
        }
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    public void getOrderSuccess(OrderListResponse response) {
        orderList.getStatusLayout().showContentView();
        orderList.setRefreshFinished();
        adapter.clearData();
        adapter.setNewData(response.list);
        if (adapter.getData().size() == 0) {
            orderList.getStatusLayout().showEmptyView(getString(R.string.no_order_yet));
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
    public void finishOrderSuccess() {
        refreshData();
    }


    private void dealWithOrangeButtonClick(int position, OrderBean orderBean) {
        switch (orderBean.status) {
            case 1:
                Intent intent = new Intent(mCtx, PayChooseActivity.class);
                intent.putExtra(Constant.ORDER_ID, orderBean.order_id);
                startActivity(intent);
                break;
            case 2:
                if (orderBean.second_payment == 0) {
                    mPresenter.finishOrder(orderBean.order_id);
                } else { //二次支付
                    Intent intent1 = new Intent(mCtx, PayChooseActivity.class);
                    intent1.putExtra(Constant.ORDER_ID, orderBean.order_id);
                    intent1.putExtra(Constant.SECOND_PAY, 2);
                    startActivity(intent1);
                }
                break;
            case 3:
                Intent intent2 = new Intent(mCtx, CommentActivity.class);
                intent2.putExtra(Constant.ORDER_BEAN, orderBean);
                startActivityForResult(intent2, 201);
                break;
        }
    }

    private void dealWithBlueButtonClick(int position, OrderBean orderBean) {
        switch (orderBean.status) {
            case 1:
                new CustomAlertDialog.Builder(mCtx).setTitle(R.string.prompt).setMessage("确认取消?")
                        .setPositiveButton(mCtx.getString(R.string.yes), (dialog, which) ->
                                mPresenter.cancelOrder(0, orderBean.order_id)).setNegativeButton(mCtx.getResources().getString(R.string.cancel), null).create().show();
                break;
            case 2:
            case 4:
                Intent intent = new Intent(mCtx, HelpRobotActivity.class);
                startActivity(intent);
                break;
            case 3:
                Intent intent1 = new Intent(mCtx, CommentActivity.class);
                intent1.putExtra(Constant.ORDER_BEAN, orderBean);
                startActivity(intent1);
                break;
        }
    }
}
