package com.wuyou.user.network;

import android.support.v4.util.ArrayMap;

import com.google.gson.GsonBuilder;
import com.gs.buluo.common.network.LogInterceptor;
import com.wuyou.user.Constant;
import com.wuyou.user.util.GsonEosTypeAdapterFactory;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2016/11/1.
 */
public class IPFSRetrofit {

    private static IPFSRetrofit instance;
    private ArrayMap<Class, Object> apis = new ArrayMap<>();
    private final Retrofit retrofit;

    private IPFSRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new LogInterceptor());
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.IPFS_URL.split("api/v0")[0])
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                        .serializeNulls().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public synchronized static IPFSRetrofit getInstance() {
        if (null == instance) {
            instance = new IPFSRetrofit();
        }
        return instance;
    }

    public <T> T createApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = retrofit.create(service);
            apis.put(service, instance);
        }

        return (T) apis.get(service);
    }

    /**
     * 线程切换
     *
     * @return
     */
    public static <T> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
