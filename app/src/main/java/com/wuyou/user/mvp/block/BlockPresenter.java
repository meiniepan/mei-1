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
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.network.ChainRetrofit;
import com.wuyou.user.network.apis.NodeosApi;
import com.wuyou.user.util.RxUtil;

import org.bson.Document;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockPresenter extends BlockContract.Presenter {
    private MongoClient mongoClient;

    public BlockPresenter() {
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
    public void detachView() {
        super.detachView();
        mongoClient.close();
    }
}
