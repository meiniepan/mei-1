package com.wuyou.user.mvp.order;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.OrderApis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderPresenter extends OrderContract.Presenter {
    @Override
    void getOrder(int type) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderList(CarefreeApplication.getInstance().getUserInfo().getUid(), type, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {
                        mView.getOrderSuccess(orderListResponseBaseResponse.data.list);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getOrderMore(int type) {

    }

    @Override
    void cancelOrder(int position, String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .cancelOrder(orderId, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        mView.cancelSuccess(position);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getOrderDetail(String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderDetail(orderId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBeanDetail>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderBeanDetail> orderResponse) {
                        mView.getOrderDetailSuccess(orderResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });

    }
}
