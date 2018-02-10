package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.FastLevelResponse;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.bean.response.ServeDetailResponse;
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

public interface ServeApis {
    @GET("category/list")
    Observable<BaseResponse<CategoryListResponse>> getCategoryList(@QueryMap SortedTreeMap<String, String> map);


    @GET("shops/{category_id}/{start_id}/{flag}")
    Observable<BaseResponse<ServeListResponse>> getServeList(@Path("category_id") String id,
                                                             @Path("start_id") String startId, @Path("flag") int flag, @QueryMap SortedTreeMap<String, String> map);

    @GET("service/{service_id}")
    Observable<BaseResponse<ServeDetailResponse>> getServeDetail(@Path("service_id") String id, @QueryMap SortedTreeMap<String, String> map);


    @GET("service/levels/{category_id}")
    Observable<BaseResponse<FastLevelResponse>> getFastServeLevel(@Path("category_id") String id, @QueryMap SortedTreeMap<String, String> map);


    @FormUrlEncoded
    @POST("order")
    Observable<BaseResponse<String>> createOrder(
            @FieldMap SortedTreeMap<String, String> map);



}
