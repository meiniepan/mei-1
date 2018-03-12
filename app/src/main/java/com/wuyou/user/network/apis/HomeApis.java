package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.ServeSites;
import com.wuyou.user.bean.response.CommunityListResponse;
import com.wuyou.user.bean.response.HomeVideoResponse;
import com.wuyou.user.bean.response.ListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface HomeApis {
    @GET("v1/communities")
    Observable<BaseResponse<CommunityListResponse>> getCommunitiesList(@QueryMap SortedTreeMap<String, String> map);

    @GET("v1/client/video/{community_id}")
    Observable<BaseResponse<HomeVideoResponse>> getVideos(
            @Path("community_id") String communityId, @QueryMap SortedTreeMap<String, String> map);


    @GET("v1/sites")
    Observable<BaseResponse<ListResponse<ServeSites>>> getServeSites(@QueryMap SortedTreeMap<String, String> map);
}
