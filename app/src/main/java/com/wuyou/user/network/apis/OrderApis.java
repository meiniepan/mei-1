package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.OrderBeanDetail;
import com.wuyou.user.bean.OrderIdBean;
import com.wuyou.user.bean.response.OrderListResponse;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn on 2018/2/6.
 */

public interface OrderApis {
    @GET("orders/{uid}/{status}/{start_id}/{flag}")
    Observable<BaseResponse<OrderListResponse>> getOrderList(@Path("uid") String uid, @Path("status") int status
            , @Path("start_id") String startId, @Path("flag") int flag, @QueryMap SortedTreeMap<String, String> map);

    @GET("order/{orderId}")
    Observable<BaseResponse<OrderBeanDetail>> getOrderDetail(@Path("orderId") String id, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("order")
    Observable<BaseResponse<OrderIdBean>> createOrder(
            @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @DELETE("order/{order_id}}")
    Observable<BaseResponse> cancelOrder(@Path("order_id") String id,
                                         @FieldMap SortedTreeMap<String, String> map);

}
