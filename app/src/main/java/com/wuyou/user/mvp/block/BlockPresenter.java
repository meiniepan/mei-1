package com.wuyou.user.mvp.block;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DELL on 2018/9/28.
 */

public class BlockPresenter extends BlockMainContract.Presenter {

    private MongoCollection<Document> collection;
    private long lineDataLastTime;

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
            public void onSuccess(String amount) {
                if (isAttach()) mView.getTransactionsAmountSuccess(amount);
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

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    @Override
    void getOriginData() {
        long east8Time = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis() - 8 * 3600 * 1000;
        lineDataLastTime = currentTime;
        String lastExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        for (int i = 1; i <= 5; i++) {
            currentTime -= 5000;
            String currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
            getFiveSecondsData(lastExpiration, currentExpiration, east8Time);
            east8Time -= 5000;
            lastExpiration = currentExpiration;
        }
    }

    private ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>();

    private void getFiveSecondsData(String lastExpiration, String currentExpiration, long east8Time) {
        Observable.create((ObservableOnSubscribe<Long>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            collection = database.getCollection("transactions");
            e.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))) / 5);
        }).compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                concurrentHashMap.put(simpleDateFormat1.format(new Date(east8Time)), aLong);
                if (concurrentHashMap.size() == 5) {
                    ArrayList<LinePoint> linePoints = new ArrayList<>();
                    for (Map.Entry<String, Long> entry : concurrentHashMap.entrySet()) {
                        linePoints.add(new LinePoint(entry.getKey(), entry.getValue() + ""));
                    }
                    Collections.sort(linePoints, (o1, o2) -> o1.x.compareTo(o2.x));
                    mView.getOriginDataSuccess(linePoints);
                }
            }
        });
    }

    @Override
    void getLastFiveSecondsData() {
        String formatTime = simpleDateFormat.format(new Date(lineDataLastTime)) + "T" + simpleDateFormat1.format(new Date(lineDataLastTime));
        lineDataLastTime += 5000;
        String newFormatTime = simpleDateFormat.format(new Date(lineDataLastTime)) + "T" + simpleDateFormat1.format(new Date(lineDataLastTime));
        String east8time = simpleDateFormat1.format(new Date(lineDataLastTime + 8 * 3600 * 1000));
        addDisposable(Observable.create((ObservableOnSubscribe<Long>) e -> {
            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            e.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", newFormatTime), Filters.gt("expiration", formatTime))) / 5);
        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<Long>() {
            @Override
            public void onSuccess(Long amount) {
                if (isAttach())
                    mView.getLastFiveSecondsDataSuccess(new LinePoint(east8time, amount + ""));
            }
        }));
    }
}
