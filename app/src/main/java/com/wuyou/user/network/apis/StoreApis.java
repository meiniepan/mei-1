package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.StoreBean;
import com.wuyou.user.bean.response.ServeListResponse;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn on 2018/2/6.
 */

public interface StoreApis {
    @GET("v1/shop/{shop_id}")
    Observable<BaseResponse<StoreBean>> getStoreDetail(@Path("shop_id") String id, @QueryMap SortedTreeMap<String, String> map);


    @GET("v1/shops/{category_id}/{start_id}/{flag}")
    Observable<BaseResponse<ServeListResponse>> getServeList(@Path("category_id") String id,
                                                             @Path("start_id") String startId, @Path("flag") int flag, @QueryMap SortedTreeMap<String, String> map);


    @FormUrlEncoded
    @POST("v1/order")
    Observable<BaseResponse<String>> createOrder(
            @FieldMap SortedTreeMap<String, String> map);



}
