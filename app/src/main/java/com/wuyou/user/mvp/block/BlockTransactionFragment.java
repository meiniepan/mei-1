package com.wuyou.user.mvp.block;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.TransactionBean;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.layoutmanager.WrapLinearLayoutManager;
import com.wuyou.user.view.fragment.BaseFragment;

import org.bson.Document;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockTransactionFragment extends BaseFragment {

    @BindView(R.id.block_transactions)
    RecyclerView recyclerView;
    @BindView(R.id.block_transactions_status)
    StatusLayout statusLayout;


    private MongoClient mongoClient;
    private BlockTransactionsAdapter adapter;
    private Disposable intervalDisposable;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_block_transcation;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(CommonUtil.getRecyclerDivider(getContext()));
        adapter = new BlockTransactionsAdapter(R.layout.item_block_transactions);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(() -> getMoreData(), recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getContext(), BlockDetailActivity.class);
                intent.putExtra(Constant.SEARCH_TEXT, adapter.getItem(i).id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void fetchData() {
        getData();
        intervalDisposable = Observable.interval(2, TimeUnit.SECONDS).subscribe(aLong -> getNewData());
    }

    TransactionBean transactionBean = null;

    public void getData() {
        statusLayout.showProgressView();
        Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transaction_traces");
            FindIterable<Document> action_traces = collection.find().sort(Sorts.descending("action_traces.receipt.global_sequence")).filter(Filters.eq("action_traces.act.name", "transfer")).limit(30);
            MongoCursor<Document> iterator = action_traces.iterator();
            e.onNext(getTransactionBeans(iterator));
        }).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> transactionBeans) {
                        statusLayout.showContentView();
                        adapter.setNewData(transactionBeans);
                        firstDate = transactionBeans.get(0).time;
                        lastDate = transactionBeans.get(transactionBeans.size() - 1).time;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    @NonNull
    private ArrayList<TransactionBean> getTransactionBeans(MongoCursor<Document> iterator) {
        ArrayList<TransactionBean> list = new ArrayList<>();
        while (iterator.hasNext()) {
            transactionBean = new TransactionBean();
            Document document = iterator.next();
            transactionBean.id = document.get("id").toString();
            ArrayList<Document> array = (ArrayList<Document>) document.get("action_traces");
            Document data = null;
            try {
                transactionBean.time = Long.parseLong(((Document) array.get(0).get("receipt")).get("global_sequence").toString());
                data = ((Document) array.get(0).get("act"));
                Document document1 = (Document) data.get("data");
                transactionBean.actor = document1.getString("from");
                transactionBean.receiver = document1.getString("to");
                transactionBean.content = document1.getString("quantity");
                list.add(transactionBean);
            } catch (Exception e1) {
                transactionBean.content = array.get(0).get("data").toString();
                list.add(transactionBean);
            }
        }
        return list;
    }

    private long firstDate;
    private long lastDate;

    public void getNewData() {
        Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            Log.e("Carefree", "getNewData: ");
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transaction_traces");
            FindIterable<Document> action_traces = collection.find().sort(Sorts.descending("action_traces.receipt.global_sequence")).filter(Filters.and(Filters.eq("action_traces.act.name", "transfer"), Filters.gt("action_traces.receipt.global_sequence", firstDate))).limit(20);
            MongoCursor<Document> iterator = action_traces.iterator();
            e.onNext(getTransactionBeans(iterator));
        }).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> transactionBeans) {
                        adapter.addData(0, transactionBeans);
                        recyclerView.scrollToPosition(0);
                        if (transactionBeans.size() > 0) firstDate = transactionBeans.get(0).time;
                    }
                });
    }

    public void getMoreData() {
        Observable.create((ObservableOnSubscribe<ArrayList<TransactionBean>>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transaction_traces");
            FindIterable<Document> action_traces = collection.find().sort(Sorts.descending("action_traces.receipt.global_sequence")).filter(Filters.and(Filters.eq("action_traces.act.name", "transfer"), Filters.lt("action_traces.receipt.global_sequence", lastDate))).limit(20);
            MongoCursor<Document> iterator = action_traces.iterator();
            e.onNext(getTransactionBeans(iterator));
        }).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<ArrayList<TransactionBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TransactionBean> transactionBeans) {
                        adapter.addData(transactionBeans);
                        if (transactionBeans.size() > 0) {
                            lastDate = transactionBeans.get(transactionBeans.size() - 1).time;
                        } else {
                            adapter.loadMoreEnd(true);
                        }
                    }
                });

    }

    private class BlockTransactionsAdapter extends BaseQuickAdapter<TransactionBean, BaseHolder> {
        public BlockTransactionsAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseHolder baseHolder, TransactionBean transactionBean) {
            baseHolder.setText(R.id.item_block_transaction_actor, transactionBean.actor)
                    .setText(R.id.item_block_transaction_receiver, transactionBean.receiver)
                    .setText(R.id.item_block_transaction_content, transactionBean.content + transactionBean.time);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mongoClient.close();
        if (intervalDisposable != null) intervalDisposable.dispose();
    }
}
