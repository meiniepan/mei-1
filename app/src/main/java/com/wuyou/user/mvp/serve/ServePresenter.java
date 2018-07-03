package com.wuyou.user.mvp.serve;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.bean.response.ServeListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.RxUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServePresenter extends ServeContract.Presenter {
    private String startId;
    private String serveId;
    private int key;
    private int sort;

    private int page = 1;

    /**
     * key    默认：0；价格：1；销量：2
     * sort    倒序：0；升序：1
     */
    @Override
    void getServe(String serveId, int key, int sort) {
        this.serveId = serveId;
        this.key = key;
        this.sort = sort;
        page = 1;
        BaseSubscriber<BaseResponse<ServeListResponse>> observer = new BaseSubscriber<BaseResponse<ServeListResponse>>() {
            @Override
            public void onSuccess(BaseResponse<ServeListResponse> orderListResponseBaseResponse) {
                ServeListResponse r = orderListResponseBaseResponse.data;
                if (isAttach()) mView.getServeSuccess(r);
                if (r.list.size() > 0) {
                    startId = r.list.get(r.list.size() - 1).service_id;
                    if (key == 0) {
                        startId = r.list.get(r.list.size() - 1).service_id;
                    } else {
                        page++;
                    }
                }
            }

            @Override
            protected void onFail(ApiException e) {
                if (isAttach()) mView.showError(e.getDisplayMessage(), e.getCode());
            }
        };

        QueryMapBuilder mapBuilder = QueryMapBuilder.getIns().put("category_id", serveId).put("key", key + "").put("sort", sort + "");
        if (key == 0) {
            mapBuilder.put("start_id", 0 + "").put("flag", 1 + "");
            CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                    .getServeList(mapBuilder.buildGet())
                    .compose(RxUtil.switchSchedulers())
                    .subscribe(observer);
        } else {
            mapBuilder.put("page_number", page + "");
            CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                    .getSortedServeList(mapBuilder.buildGet())
                    .compose(RxUtil.switchSchedulers())
                    .subscribe(observer);
        }
    }

    @Override
    void getServeMore() {
        BaseSubscriber<BaseResponse<ServeListResponse>> observer = new BaseSubscriber<BaseResponse<ServeListResponse>>() {
            @Override
            public void onSuccess(BaseResponse<ServeListResponse> orderListResponseBaseResponse) {
                ServeListResponse data = orderListResponseBaseResponse.data;
                if (isAttach()) mView.loadMore(data);
                if (data.list.size() > 0) {
                    if (key == 0) {
                        startId = data.list.get(data.list.size() - 1).service_id;
                    } else {
                        page++;
                    }
                }
            }

            @Override
            protected void onFail(ApiException e) {
                if (isAttach()) mView.loadMoreFail(e.getDisplayMessage(), e.getCode());
            }
        };

        QueryMapBuilder mapBuilder = QueryMapBuilder.getIns().put("category_id", serveId).put("key", key + "").put("sort", sort + "");
        if (key == 0) {
            mapBuilder.put("start_id", startId).put("flag", 2 + "");
            CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                    .getServeList(mapBuilder.buildGet())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            mapBuilder.put("page_number", page + "");
            CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                    .getSortedServeList(mapBuilder.buildGet())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }

    }
}
