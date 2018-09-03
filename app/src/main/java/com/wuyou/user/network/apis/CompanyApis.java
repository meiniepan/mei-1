package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.wuyou.user.data.remote.Company;
import com.wuyou.user.data.local.db.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface CompanyApis {
    @GET("v1/companies")
    Observable<List<Company>> getCompaniesList(
            @Query("communityId") String communityId);

    @POST("v1/persons/{id}/company_bind_request")
    Observable<BaseResponse<UserInfo>> bindCompany(
            @Path("id") String id);

}
