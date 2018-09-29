package com.wuyou.user.mvp.block;

import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.gs.buluo.common.network.BaseSubscriber;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosChainInfo;
import com.wuyou.user.data.local.LinePoint;
import com.wuyou.user.util.RxUtil;

import org.bson.Document;
import org.reactivestreams.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/9/28.
 */

public class BlockPresenter extends BlockMainContract.Presenter {

    private MongoCollection<Document> collection;

    @Override
    void getBlockHeight() {
        addDisposable(EoscDataManager.getIns().getChainInfo().compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<EosChainInfo>() {
            @Override
            public void onSuccess(EosChainInfo eosChainInfo) {
                if (isAttach()) mView.getBlockHeightSuccess(eosChainInfo.getHeadBlockNum() + "");
            }
        }));
    }

    @Override
    void getTransactionsAmount() {
        addDisposable(Observable.create((ObservableOnSubscribe<String>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            e.onNext(collection.countDocuments() + "");
        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                if (isAttach()) mView.getTransactionsAmountSuccess(s);
            }
        }));
    }

    @Override
    void getAccountAmount() {
        addDisposable(Observable.create((ObservableOnSubscribe<String>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("accounts");
            e.onNext(collection.countDocuments() + "");
        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                if (isAttach()) mView.getAccountAmountSuccess(s);
            }
        }));
    }

    @Override
    void getPointTypeAmount() {
        addDisposable(Observable.create((ObservableOnSubscribe<String>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("action_traces");
            e.onNext(collection.countDocuments(Filters.eq("act.name", "issue")) + "");
        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                if (isAttach()) mView.getPointTypeAmountSuccess(s);
            }
        }));
    }

    private MongoClient mongoClient;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    private long currentTime;

    private long time;
    private ConcurrentHashMap<String,Long> concurrentHashMap;

    @Override
    void getOriginData() {
        ArraySet arraySet = new ArraySet();
        time = System.currentTimeMillis();
        currentTime = System.currentTimeMillis() - 8 * 3600 * 1000;
        String lastExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        currentTime -= 5000;
        String currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        Flowable<Long> source1 = executeQuery(lastExpiration, currentExpiration).subscribeOn(Schedulers.newThread());
        lastExpiration = currentExpiration;
        currentTime -= 5000;
        currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        Flowable<Long> source2 = executeQuery(lastExpiration, currentExpiration).subscribeOn(Schedulers.newThread());
        lastExpiration = currentExpiration;
        currentTime -= 5000;
        currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        Flowable<Long> source3 = executeQuery(lastExpiration, currentExpiration).subscribeOn(Schedulers.newThread());
        lastExpiration = currentExpiration;
        currentTime -= 5000;
        currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        Flowable<Long> source4 = executeQuery(lastExpiration, currentExpiration).subscribeOn(Schedulers.newThread());
        lastExpiration = currentExpiration;
        currentTime -= 5000;
        currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        Flowable<Long> source5 = executeQuery(lastExpiration, currentExpiration).subscribeOn(Schedulers.newThread());

        Flowable<Long> merge = Flowable.merge(source1, source2, source3, source4).mergeWith(source5);

        Flowable.create((FlowableOnSubscribe<String>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            collection = database.getCollection("transactions");
            e.onNext("");
        }, BackpressureStrategy.BUFFER).flatMap(s -> merge).subscribeOn(Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).
                subscribe(aLong -> {
                    Log.e("Carefree", "onSuccess: " + aLong + "..............." + (System.currentTimeMillis() - time));
                    arraySet.add(aLong);
                    if (arraySet.size() == 5) {
                        mView.getOriginDataSuccess(arraySet);
                    }
                }, throwable -> mView.showError("数据库请求失败", 500), () -> {});
    }

    @NonNull
    private Flowable<Long> executeQuery(String lastExpiration, String currentExpiration) {
        return new Flowable<Long>() {
            @Override
            protected void subscribeActual(Subscriber<? super Long> s) {
                Log.e("Carefree", "subscribeActual: " + lastExpiration + "...." + currentExpiration + "....." + Thread.currentThread());
                s.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        };
    }
}
