package com.wuyou.user.mvp.serve;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.bean.response.ServeListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServePresenter extends ServeContract.Presenter {
    private String startId;
    private String serveId;

    @Override
    void getServe(String serveId) {
        this.serveId = serveId;
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeList(serveId, 0 + "", 1, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeListResponse> orderListResponseBaseResponse) {
                        ServeListResponse r = orderListResponseBaseResponse.data;
                        mView.getServeSuccess(r);
                        if (r.list.size() > 0) startId = r.list.get(0).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getServeMore() {
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeList(serveId, startId + "", 2, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeListResponse> orderListResponseBaseResponse) {
                        ServeListResponse data = orderListResponseBaseResponse.data;
                        mView.loadMore(data);
                        if (data.list.size() > 0) startId = data.list.get(0).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.loadMoreFail(e.getDisplayMessage(), e.getCode());
                    }
                });
    }
}
