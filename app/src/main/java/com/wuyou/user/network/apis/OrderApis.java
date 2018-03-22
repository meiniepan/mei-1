package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.OrderIdBean;
import com.wuyou.user.bean.response.OrderListResponse;

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
    @GET("orders")
    Observable<BaseResponse<OrderListResponse>> getOrderList(@QueryMap SortedTreeMap<String, String> map);

    @GET("order/{orderId}")
    Observable<BaseResponse<OrderBeanDetail>> getOrderDetail(@Path("orderId") String id, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("order/{uid}")
    Observable<BaseResponse<OrderIdBean>> createOrder(@Path("uid") String uid,
                                                      @FieldMap SortedTreeMap<String, String> map);

    @HTTP(method = "DELETE", path = "order/{order_id}", hasBody = true)
    Observable<BaseResponse> deletelOrder(@Path("order_id") String id,
                                          @Body SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("order/{order_id}")
    Observable<BaseResponse> cancelOrder(@Path("order_id") String id,
                                         @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("order/pay/{order_id}")
    Observable<BaseResponse> payOrder(@Path("order_id") String id,
                                      @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("order/finish/{uid}/{order_id}")
    Observable<BaseResponse> finishOrder(@Path("uid") String uid, @Path("order_id") String id,
                                         @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("order/{uid}")
    Observable<BaseResponse> createComment(@Path("uid") String uid,
                                                      @FieldMap SortedTreeMap<String, String> map);
}
