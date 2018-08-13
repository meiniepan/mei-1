package com.wuyou.user.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.user.bean.UpdateInfo;
import com.wuyou.user.bean.response.AddressListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by DELL on 2018/7/26.
 */

public interface ConfigApis {
    @GET("v1/client/update")
    Observable<BaseResponse<UpdateInfo>> getUpdateInfo(@QueryMap SortedTreeMap<String, String> map);
}
