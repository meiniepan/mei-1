package com.wuyou.user.network;

import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.util.CommonUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjn on 2016/11/10.
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder builder = req.newBuilder();
        HttpUrl url = req.url();
        if (CarefreeApplication.getInstance().getUserInfo() != null && CarefreeApplication.getInstance().getUserInfo().getToken() != null) {
            builder.addHeader("Authorization", CarefreeApplication.getInstance().getUserInfo().getToken());
            if (url.encodedPath().contains("wallets") || url.encodedPath().contains("recharge")||url.encodedPath().contains("conference_rooms")) {
                HttpUrl.Builder newBuilder = url.newBuilder();
                newBuilder.addQueryParameter("me", CarefreeApplication.getInstance().getUserInfo().getId());
                url = newBuilder.build();
            }
        }
        Request request = builder.addHeader("Accept", "application/json").url(url).addHeader("Content-Type", "application/json").
                addHeader("User-Agent", CommonUtil.getDeviceInfo(CarefreeApplication.getInstance().getApplicationContext())).build();
        return chain.proceed(request);
    }
}
