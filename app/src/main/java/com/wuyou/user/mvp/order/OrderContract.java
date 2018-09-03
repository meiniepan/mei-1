package com.wuyou.user.mvp.order;

import com.wuyou.user.data.remote.OrderBeanDetail;
import com.wuyou.user.data.remote.response.OrderListResponse;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface OrderContract {
    interface View extends IBaseView {
        void getOrderSuccess(OrderListResponse list);

        void loadMore(OrderListResponse data);

        void cancelSuccess(int position);

        void getOrderDetailSuccess(OrderBeanDetail bean);

        void loadMoreFail(String displayMessage, int code);

        void finishOrderSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getOrder(int type);

        abstract void getOrderMore(int type);

        abstract void cancelOrder(int position, String orderId);

        abstract void getOrderDetail(String orderId);

        abstract void finishOrder(String orderId);
    }
}
