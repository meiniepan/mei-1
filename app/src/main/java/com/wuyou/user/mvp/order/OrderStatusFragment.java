package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<OrderBean> list = new ArrayList<>();
        adapter = new OrderListAdapter(R.layout.item_order_list, list);
        orderList.setAdapter(adapter);
        orderListLayout.showProgressView();
        mPresenter.getOrder(type);
        orderList.setRefreshAction(() -> {
            mPresenter.getOrder(type);
        });
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            dealWithClick(position, (OrderBean) adapter.getData().get(position));
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            OrderBean bean = (OrderBean) adapter.getData().get(position);
            Intent intent = new Intent(mCtx, OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, bean.id);
            startActivity(intent);
        });
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(mCtx, message);
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    public void getOrderSuccess(List<OrderBean> list) {
        adapter.clearData();
        adapter.setNewData(list);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void cancelSuccess(int pos) {
        adapter.remove(pos);
    }


    //详情接口回调，忽略
    @Override
    public void getOrderDetailSuccess(OrderBeanDetail bean) {
    }

    private void dealWithClick(int position, OrderBean orderBean) {
        switch (orderBean.status) {
            case "待确认":
            case "待服务":
                mPresenter.cancelOrder(position, orderBean.id);
                break;
            case "待支付":

                break;
            case "待评价":

                break;
        }
    }
}
