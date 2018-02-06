package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.response.OrderListResponse;

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

public interface ServeApis {
    @GET("category/list")
    Observable<BaseResponse<OrderListResponse>> getCategotyList(@QueryMap SortedTreeMap<String, String> map);


    @GET("category/list/{category_id}")
    Observable<BaseResponse<OrderListResponse>> getServeList(@Path("category_id") String id, @QueryMap SortedTreeMap<String, String> map);


    @FormUrlEncoded
    @POST("order")
    Observable<BaseResponse<String>> createOrder(
            @FieldMap SortedTreeMap<String, String> map);

}
