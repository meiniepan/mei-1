package com.wuyou.user.mvp.order;

import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface OrderContract {
    interface View extends IBaseView {
        void getOrderSuccess(List<OrderBean> list);

        void loadMore();

        void cancelSuccess(int position);

        void getOrderDetailSuccess(OrderBeanDetail bean);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getOrder(int type);

        abstract void getOrderMore(int type);

        abstract void cancelOrder(int position, String orderId);

        void getOrderDetail(String orderId) {
        }
    }
}
