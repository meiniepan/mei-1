package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.StatusLayout;
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
    StatusLayout orderListStatus;
    private int type;
    private OrderListAdapter adapter;
    private PayPanel payPanel;

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
        orderListStatus.setLoginAction(v -> {
            Intent intent = new Intent(mCtx, LoginActivity.class);
            startActivity(intent);
        });
        orderListStatus.getLoginActView().setText(R.string.login_now);
        orderListStatus.setErrorAndEmptyAction(v -> {
            orderListStatus.showProgressView();
            mPresenter.getOrder(type);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderListStatus.showLoginView(getString(R.string.no_login));
            orderList.setRefreshEnable(false);
        } else {
            orderListStatus.showProgressView();
            mPresenter.getOrder(type);
            orderList.setRefreshEnable(true);
        }
    }

    @Override
    public void fetchData() {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            orderListStatus.showLoginView(getString(R.string.no_login));
            return;
        }
        orderListStatus.showProgressView();
        mPresenter.getOrder(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    private void refreshData() {
        orderListStatus.showProgressView();
        mPresenter.getOrder(type);
    }

    @Override
    public void showError(String message, int res) {
        if (res == 100) {
            ToastUtils.ToastMessage(mCtx, message);
        } else {
            orderListStatus.showErrorView(message);
        }
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    public void getOrderSuccess(OrderListResponse response) {
        orderListStatus.showContentView();
        orderList.setRefreshFinished();
        adapter.clearData();
        adapter.setNewData(response.list);
        if (adapter.getData().size() == 0) {
            orderListStatus.showEmptyView(getString(R.string.no_order_yet));
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


    private void dealWithOrangeButtonClick(int position, OrderBean orderBean) {
        switch (orderBean.status) {
            case 1:
                payPanel = new PayPanel(mCtx, new PayPanel.OnPayFinishListener() {
                    @Override
                    public void onPaying() {
                        mPresenter.payOrder(orderBean.order_id, orderBean.serial);
                        payPanel.dismiss();
                    }

                    @Override
                    public void onPayFail(ApiException e) {
                        payPanel.dismiss();
                    }
                });
                payPanel.show();
                break;
            case 2:
                mPresenter.finishOrder(orderBean.order_id);
                break;
            case 3:
                Intent intent = new Intent(mCtx, CommentActivity.class);
                intent.putExtra(Constant.ORDER_BEAN, orderBean);
                startActivity(intent);
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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constant.HELP_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
