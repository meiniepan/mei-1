package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.data.remote.WalletBalance;
import com.wuyou.user.data.remote.response.SimpleResponse;
import com.wuyou.user.data.remote.response.WxPayResponse;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by DELL on 2018/3/27.
 */

public interface MoneyApis {
    @GET("v1/coin/balance/{uid}")
    Observable<BaseResponse<WalletBalance>> getWalletBalance(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/alipay/order/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getAliPayOrderInfo(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/wx_pay/order/{order_id}")
    Observable<BaseResponse<WxPayResponse>> getWXPayOrderInfo(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/wx_pay/activity_order/{order_id}")
    Observable<BaseResponse<WxPayResponse>> getActivityWXPayOrderInfo(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/login")
    Observable<BaseResponse<UserInfo>> doLogin(
            @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/login/{uid}")
    Observable<BaseResponse> doLogout(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @GET("v1/is_paid/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getPayStatus(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/activity_order/is_paid/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getActivityPayStatus(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/alipay/activity_order/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getActivityAliPayOrderInfo(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);
}
