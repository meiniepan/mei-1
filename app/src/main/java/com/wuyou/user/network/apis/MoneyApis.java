package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.bean.WalletBalance;
import com.wuyou.user.bean.response.SimpleResponse;

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
    @GET("coin/balance/{uid}")
    Observable<BaseResponse<WalletBalance>> getWalletBalance(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    @GET("alipay/order/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getAliPayOrderInfo(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<UserInfo>> doLogin(
            @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("login/{uid}")
    Observable<BaseResponse> doLogout(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @GET("is_paid/{order_id}")
    Observable<BaseResponse<SimpleResponse>> getPayStatus(
            @Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);
}
