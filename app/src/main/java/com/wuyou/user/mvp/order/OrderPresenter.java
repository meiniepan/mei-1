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

    private String startId;

    @Override
    void getOrder(int type) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderList(CarefreeApplication.getInstance().getUserInfo().getUid(), type, 0 + "", 1, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {
                        OrderListResponse r = orderListResponseBaseResponse.data;
                        mView.getOrderSuccess(r);
                        if (r.list.size() > 0) startId = r.list.get(r.list.size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getOrderMore(int type) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderList(CarefreeApplication.getInstance().getUserInfo().getUid(), type, startId + "", 2, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {
                        OrderListResponse data = orderListResponseBaseResponse.data;
                        mView.loadMore(data);
                        if (data.list.size() > 0) startId = data.list.get(data.list.size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.loadMoreFail(e.getDisplayMessage(), e.getCode());
                    }
                });
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

    @Override
    void payOrder(String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .payOrder(orderId, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        mView.paySuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), 100);
                    }
                });
    }

    @Override
    void finishOrder(String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .finishOrder(CarefreeApplication.getInstance().getUserInfo().getUid(), orderId, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        mView.finishOrderSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), 100);
                    }
                });
    }


}
