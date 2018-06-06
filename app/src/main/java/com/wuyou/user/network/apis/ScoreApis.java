package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.PointBean;
import com.wuyou.user.bean.ScoreRecordBean;
import com.wuyou.user.bean.SignRecordBean;
import com.wuyou.user.bean.response.ListResponse;

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

public interface ScoreApis {
    @GET("received_points/{uid}")
    Observable<BaseResponse<ListResponse<ScoreRecordBean>>> getScoreRecordList(@Path("uid") String uid,
                                                                               @QueryMap SortedTreeMap<String, String> map);


    @FormUrlEncoded
    @POST("sign")
    Observable<BaseResponse<PointBean>> signIn(
            @FieldMap SortedTreeMap<String, String> map);

    @GET("sign/list/{uid}")
    Observable<BaseResponse<ListResponse<SignRecordBean>>> getSignInRecord(@Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);
}
