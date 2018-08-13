package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.AddressId;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.bean.response.CityListResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface AddressApis {
    @GET("v1/addresses/{uid}")
    Observable<BaseResponse<AddressListResponse>> getAddressList(
            @Path("uid") String uid,@QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("v1/address/{uid}")
    Observable<BaseResponse<AddressId>> addAddress(@Path("uid") String uid,
                                                   @FieldMap SortedTreeMap<String,String> map);

    @FormUrlEncoded
    @PUT("v1/address/{uid}/{address_id}")
    Observable<BaseResponse> updateAddress(@Path("uid") String uid, @Path("address_id") String addressId,
                                           @FieldMap SortedTreeMap<String,String> map);

    @HTTP(method = "DELETE", path = "v1/address/{uid}/{address_id}", hasBody = true)
    Observable<BaseResponse> deleteAddress(@Path("uid") String uid, @Path("address_id") String id,
                                           @Body SortedTreeMap<String, String> map);


    @GET("v1/client/cities")
    Observable<BaseResponse<CityListResponse>> getCityList(@QueryMap SortedTreeMap<String, String> map);
}
