package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.data.remote.OrderBeanDetail;
import com.wuyou.user.data.remote.OrderIdBean;
import com.wuyou.user.data.remote.response.OrderListResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn on 2018/2/6.
 */

public interface OrderApis {
    @GET("v1/orders")
    Observable<BaseResponse<OrderListResponse>> getOrderList(@QueryMap SortedTreeMap<String, String> map);

    @GET("v1/order/{orderId}")
    Observable<BaseResponse<OrderBeanDetail>> getOrderDetail(@Path("orderId") String id, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/order/{uid}")
    Observable<BaseResponse<OrderIdBean>> createOrder(@Path("uid") String uid,
                                                      @FieldMap SortedTreeMap<String, String> map);

    @HTTP(method = "DELETE", path = "v1/order/{order_id}", hasBody = true)
    Observable<BaseResponse> deletelOrder(@Path("order_id") String id,
                                          @Body SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/order/cancel/{order_id}")
    Observable<BaseResponse> cancelOrder(@Path("order_id") String id,
                                         @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/order/pay/{order_id}")
    Observable<BaseResponse> payOrder(@Path("order_id") String id,
                                      @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/order/finish/{order_id}")
    Observable<BaseResponse> finishOrder(@Path("order_id") String id,
                                         @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/comment/{uid}")
    Observable<BaseResponse> createComment(@Path("uid") String uid,
                                                      @FieldMap SortedTreeMap<String, String> map);
}
