package com.wuyou.user.mvp.block;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.data.remote.TransactionBean;
import com.wuyou.user.network.ChainRetrofit;
import com.wuyou.user.network.apis.NodeosApi;
import com.wuyou.user.util.RxUtil;

import org.bson.Document;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockDetailPresenter extends BlockContract.Presenter {
    private MongoClient mongoClient;

    public BlockDetailPresenter() {
        this.mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
    }

    @Override
    void getBlockInfo(String searchText) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("block_num_or_id", searchText);
        addDisposable(ChainRetrofit.getInstance().createApi(NodeosApi.class).getBlock(jsonObject)
                .compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<BlockInfo>() {
                    @Override
                    public void onSuccess(BlockInfo blockInfo) {
                        if (isAttach()) mView.getBlockInfoSuccess(blockInfo);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError("查询不到当前区块信息", e.getCode());
                    }
                }));
    }

    @Override
    void getTransactionInfo(String searchText) {
        addDisposable(Observable.create((ObservableOnSubscribe<Document>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            FindIterable<Document> action_traces = collection.find().filter(Filters.eq("trx_id", searchText));
            MongoCursor<Document> iterator = action_traces.iterator();
            if (iterator.hasNext()) {
                e.onNext(iterator.next());
            } else {
                e.onError(new ApiException(600, "no result", ""));
            }
        }).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<Document>() {
                    @Override
                    public void onSuccess(Document document) {
                        if (isAttach()) mView.getTransactionSuccess(document);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError("查询不到当前交易信息", e.getCode());
                    }
                }));

    }

    @Override
    void getAccountInfo(String searchText) {
        addDisposable(EoscDataManager.getIns().readAccountInfo(searchText).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<EosAccountInfo>() {
                    @Override
                    public void onSuccess(EosAccountInfo eosAccountInfo) {
                        if (isAttach()) mView.getAccountInfoSuccess(eosAccountInfo);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError("查询不到当前账户信息", e.getCode());
                    }
                }));
    }

    @Override
    void getAccountTransfers(String accountName) {
        addDisposable(Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            FindIterable<Document> action_traces = collection.find().filter(Filters.eq("actions.authorization.actor", accountName)).sort(Sorts.descending("expiration")).limit(20);
            MongoCursor<Document> iterator = action_traces.iterator();
            ArrayList<TransactionBean> transactionBeans = new ArrayList<>();
            TransactionBean bean;
            while (iterator.hasNext()) {
                bean = new TransactionBean();
                Document document = iterator.next();
                bean.expiration = document.getString("expiration");
                bean.id = document.getString("trx_id");
                transactionBeans.add(bean);
            }
            e.onNext(transactionBeans);
        }).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> document) {
                        if (document.size() > 0) {
                            lastTime = document.get(document.size() - 1).expiration;
                        }
                        if (isAttach()) mView.getAccountTransactionsSuccess(document);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(), 501);
                    }
                }));
    }

    private String lastTime;

    @Override
    void getAccountTransferMore(String accountName) {
        addDisposable(Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            FindIterable<Document> action_traces = collection.find().sort(Sorts.descending("expiration")).filter(Filters.and(Filters.eq("actions.authorization.actor", accountName), Filters.lt("expiration", lastTime))).limit(20);
            MongoCursor<Document> iterator = action_traces.iterator();
            ArrayList<TransactionBean> transactionBeans = new ArrayList<>();
            TransactionBean bean;
            while (iterator.hasNext()) {
                bean = new TransactionBean();
                Document document = iterator.next();
                bean.expiration = document.getString("expiration");
                bean.id = document.getString("trx_id");
                transactionBeans.add(bean);
            }
            e.onNext(transactionBeans);
        }).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> document) {
                        if (document.size() > 0) {
                            lastTime = document.get(document.size() - 1).expiration;
                        }
                        if (isAttach()) mView.getAccountTransactionsMore(document);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(), 501);
                    }
                }));
    }

    @Override
    void getBlockTransfer(int blockNumber) {
        addDisposable(Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transactions");
            FindIterable<Document> action_traces = collection.find().filter(Filters.eq("ref_block_num", blockNumber));
            MongoCursor<Document> iterator = action_traces.iterator();
            ArrayList<TransactionBean> transactionBeans = new ArrayList<>();
            TransactionBean bean;
            while (iterator.hasNext()) {
                bean = new TransactionBean();
                Document document = iterator.next();
                bean.expiration = document.getString("expiration");
                bean.id = document.getString("trx_id");
                transactionBeans.add(bean);
            }
            e.onNext(transactionBeans);
        }).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> document) {
                        if (isAttach()) mView.getBlockTransactionSuccess(document);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(), 501);
                    }
                }));
    }

    @Override
    public void detachView() {
        super.detachView();
        mongoClient.close();
    }
}
