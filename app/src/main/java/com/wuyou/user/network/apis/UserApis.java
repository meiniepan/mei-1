package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.data.remote.UpdateEntity;
import com.wuyou.user.data.local.db.UserInfo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn91 on 2018/1/30.
 */

public interface UserApis {
    @GET("v1/login/captcha")
    Observable<BaseResponse<UserInfo>> getVerifyCode(
            @QueryMap SortedTreeMap<String, String> map);

    @GET("v1/profile/{uid}")
    Observable<BaseResponse<UserInfo>> getUserInfo(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/login")
    Observable<BaseResponse<UserInfo>> doLogin(
            @FieldMap SortedTreeMap<String, String> map);
    @FormUrlEncoded
    @PUT("v1/logout/{uid}")
    Observable<BaseResponse> doLogout(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/profile/{uid}")
    Observable<BaseResponse> updateUserInfo(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("v1/profile/edit/{uid}")
    Observable<BaseResponse> updatePwd(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);


    @GET("v1/captcha")
    Observable<BaseResponse> getCaptchaCode(
            @QueryMap SortedTreeMap<String, String> map);

    @Multipart
    @POST("v1/avatar/{uid}")
    Observable<BaseResponse> updateAvatar(
            @Path("uid")String uid,
            @Part MultipartBody.Part file,
            @QueryMap SortedTreeMap<String, String> map);
    @GET("v1/client/update")
    Observable<BaseResponse<UpdateEntity>> checkUpdate(
            @QueryMap SortedTreeMap<String, String> map);

}


