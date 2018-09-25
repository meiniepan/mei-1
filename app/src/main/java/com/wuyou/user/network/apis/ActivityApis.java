package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.data.remote.ActivityBean;
import com.wuyou.user.data.remote.ActivityListBean;
import com.wuyou.user.data.remote.response.ListResponse;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface ActivityApis {
    @GET("v2/activity/recommend")
    Observable<BaseResponse<ListResponse<ActivityBean>>> getActivityData(@QueryMap SortedTreeMap<String, String> map);

    @GET("v2/activities")
    Observable<BaseResponse<ListResponse<ActivityListBean>>> getActivityList(@QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/activity_order/receive_points/{order_id}")
    Observable<BaseResponse> updateActivityStatus(@Path("order_id")String orderId,
                                                  @FieldMap SortedTreeMap<String, String> map);


}
