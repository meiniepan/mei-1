package com.wuyou.user.mvp.block;

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

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function5;
import io.reactivex.parallel.ParallelFlowable;

/**
 * Created by DELL on 2018/9/28.
 */

public class BlockPresenter extends BlockMainContract.Presenter {

    private String currentExpiration;
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
    ArrayList<LinePoint> pointValues = new ArrayList<>();

    private long totalAmount;

    private long time;

    private String lastExpiration;

    @Override
    void getOriginData() {
        time = System.currentTimeMillis();
        currentTime = System.currentTimeMillis() - 8 * 3600 * 1000;
        lastExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
        currentTime -= 5000;
        currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));

        Observable<ArrayList<LinePoint>> zipObserve = Observable.zip(new ObservableSource<Long>() {
            @Override
            public void subscribe(Observer<? super Long> observer) {
                Log.e("Carefree", "subscribe111: "+Thread.currentThread());
                observer.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        }, new ObservableSource<Long>() {
            @Override
            public void subscribe(Observer<? super Long> observer) {
                Log.e("Carefree", "subscribe222: "+Thread.currentThread());
                observer.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        }, new ObservableSource<Long>() {
            @Override
            public void subscribe(Observer<? super Long> observer) {
                Log.e("Carefree", "subscribe333: "+Thread.currentThread());
                observer.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        }, new ObservableSource<Long>() {
            @Override
            public void subscribe(Observer<? super Long> observer) {
                Log.e("Carefree", "subscribe444: "+Thread.currentThread());
                observer.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        }, new ObservableSource<Long>() {
            @Override
            public void subscribe(Observer<? super Long> observer) {
                Log.e("Carefree", "subscribe555: "+Thread.currentThread());
                observer.onNext(collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration))));
            }
        }, new Function5<Long, Long, Long, Long, Long, ArrayList<LinePoint>>() {
            @Override
            public ArrayList<LinePoint> apply(Long aLong, Long aLong2, Long aLong3, Long aLong4, Long aLong5) throws Exception {
                return new ArrayList<>();
            }
        });
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
                MongoDatabase database = mongoClient.getDatabase("EOS");
                collection = database.getCollection("transactions");
                e.onNext("");
            }
        }).flatMap(new Function<String, ObservableSource<ArrayList<LinePoint>>>() {
            @Override
            public ObservableSource<ArrayList<LinePoint>> apply(String s) throws Exception {
                return zipObserve;
            }
        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<ArrayList<LinePoint>>() {
            @Override
            public void onSuccess(ArrayList<LinePoint> points) {
                Log.e("Carefree", "onSuccess: " + (System.currentTimeMillis() - time));
            }
        });

//        addDisposable(Observable.create((ObservableOnSubscribe<Long>) e -> {
//            currentTime = System.currentTimeMillis() - 8 * 3600 * 1000;
//            lastExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//            currentTime -= 5000;
//            currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//            MongoClient mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
//            MongoDatabase database = mongoClient.getDatabase("EOS");
//            collection = database.getCollection("transactions");
//
//            e.onNext(collection.countDocuments(Filters.and(Sorts.descending("expiration"), Filters.lte("expiration", currentExpiration), Filters.gt("expiration", lastExpiration))));
//        }).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long amount) throws Exception {
//                totalAmount = amount;
//                lastExpiration = currentExpiration;
//                currentTime -= 5000;
//                currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//                pointValues.add(new LinePoint(currentExpiration, ""));
//
//                return collection.countDocuments(Filters.and(Sorts.descending("expiration"), Filters.lte("expiration", currentExpiration), Filters.gt("expiration", lastExpiration)));
//            }
//        }).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long amount) throws Exception {
//                long firstTps = (totalAmount - amount) / 5;
//                totalAmount = amount;
//                pointValues.get(0).y = firstTps + "";
//                totalAmount = amount;
//
//                lastExpiration = currentExpiration;
//                currentTime -= 5000;
//                currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//
//                pointValues.add(new LinePoint(currentExpiration, ""));
//                return collection.countDocuments(Filters.and(Filters.lte("expiration", lastExpiration), Filters.gt("expiration", currentExpiration)));
//            }
//        }).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long amount) throws Exception {
//                Log.e("Carefree", "apply: " + amount);
//                long secondTps = (totalAmount - amount) / 5;
//                totalAmount = amount;
//                pointValues.get(1).y = secondTps + "";
//                totalAmount = amount;
//
//                lastExpiration = currentExpiration;
//                currentTime -= 5000;
//                currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//
//                pointValues.add(new LinePoint(currentExpiration, ""));
//                return collection.countDocuments(Filters.and(Sorts.descending("expiration"), Filters.lte("expiration", currentExpiration), Filters.gt("expiration", lastExpiration)));
//            }
//        }).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long amount) throws Exception {
//                long thirdTps = (totalAmount - amount) / 5;
//                totalAmount = amount;
//                pointValues.get(2).y = thirdTps + "";
//                totalAmount = amount;
//
//                lastExpiration = currentExpiration;
//                currentTime -= 5000;
//                currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//
//                pointValues.add(new LinePoint(currentExpiration, ""));
//                return collection.countDocuments(Filters.and(Sorts.descending("expiration"), Filters.lte("expiration", currentExpiration), Filters.gt("expiration", lastExpiration)));
//            }
//        }).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long amount) throws Exception {
//                long fourthTps = (totalAmount - amount) / 5;
//                totalAmount = amount;
//                pointValues.get(3).y = fourthTps + "";
//
//                lastExpiration = currentExpiration;
//                currentTime -= 5000;
//                currentExpiration = simpleDateFormat.format(new Date(currentTime)) + "T" + simpleDateFormat1.format(new Date(currentTime));
//
//                pointValues.add(new LinePoint(currentExpiration, ""));
//                return collection.countDocuments(Filters.and(Sorts.descending("expiration"), Filters.lte("expiration", currentExpiration), Filters.gt("expiration", lastExpiration)));
//            }
//        }).map(new Function<Long, ArrayList<LinePoint>>() {
//            @Override
//            public ArrayList<LinePoint> apply(Long amount) throws Exception {
//                long fifthTps = (totalAmount - amount) / 5;
//                totalAmount = amount;
//                pointValues.get(4).y = fifthTps + "";
//                totalAmount = amount;
//                return pointValues;
//            }
//        }).compose(RxUtil.switchSchedulers()).subscribeWith(new BaseSubscriber<ArrayList<LinePoint>>() {
//            @Override
//            public void onSuccess(ArrayList<LinePoint> points) {
//                Log.e("Carefree", "onSuccess: " + (System.currentTimeMillis() - time));
//                if (isAttach()) mView.getOriginDataSuccess(points);
//            }
//        }));
    }
}
