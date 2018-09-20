package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.remote.ScoreRecordBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Solang on 2018/6/1.
 */

public class ScoreRecordActivity extends BaseActivity {
    @BindView(R.id.score_record_tab)
    TabLayout scoreRecordTab;
    @BindView(R.id.score_record_pager)
    ViewPager scoreRecordPager;

    private String[] titles = {"获取记录", "积分支出"};
    private SimpleChoosePanel panel;
    private ScoreRecordAdapter obtainAdapter;
    private ScoreRecordAdapter consumeAdapter;
    private CarefreeRecyclerView obtainRecyclerView;
    private CarefreeRecyclerView consumeRecyclerView;

    private BaseSubscriber<List<ScoreRecordBean>> subscriber;
    private MongoClient mongoClient;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    private String currentAccount;
    private ScoreRecordBean recordBean;

    ArrayList<ScoreRecordBean> recordBeans = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleIcon(R.mipmap.score_record_switch, v -> switchAccount());
        EosAccount mainAccount = CarefreeDaoSession.getInstance().getMainAccount();
        scoreRecordPager.setAdapter(new ScoreRecordPagerAdapter());
        scoreRecordTab.setupWithViewPager(scoreRecordPager);

        mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
        currentAccount = mainAccount.getName();
        setTitleText(currentAccount);
        getData();
    }

    void getTableData() {
        EoscDataManager.getIns().getTable("dailyrewards", "dailyrewards", "daily", "hhhhhhhhhhh1", "", "", 100)
                .compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("Carefree", "onSuccess: " + s);
                    }
                });
    }

    private boolean isProgressing = true;
    private int totalSize = 0;
    private final int MAX_QUERY_AMOUNT = 10000;

    private void getData() {
        subscriber = new BaseSubscriber<List<ScoreRecordBean>>() {
            @Override
            public void onSuccess(List<ScoreRecordBean> data) {
                if (data.size() != 0) {
                    if (isProgressing) {
                        obtainRecyclerView.showContentView();
                        recordBeans.clear();
                        isProgressing = false;
                    }
                    obtainAdapter.addData(data);
                    totalSize += 10;
//                    if (totalSize >= MAX_QUERY_AMOUNT) {
//                        mongoClient.close();//换账号问题
//                    }
                }
            }


            @Override
            protected void onFail(ApiException e) {
                //此处因为有 mongoClient.close(); 故有java.lang.IllegalStateException: state should be: open
                //因为不需要查询太多，当达到 MAX_QUERY_AMOUNT 时即关闭数据库查询 防止数据量过大 而且没什么用 所以不需要弹错误提示
                if (!e.getType().contains("state should be: open")) {
                    ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                }
            }

            @Override
            public void onComplete() {
                if (isProgressing) {
                    isProgressing = false;
                    obtainRecyclerView.showContentView();
                }
                obtainAdapter.addData(recordBeans);
                if (obtainAdapter.getData().size() == 0) {
                    obtainRecyclerView.showEmptyView(getString(R.string.no_score));
                }
            }
        };
        Observable.create((ObservableOnSubscribe<ScoreRecordBean>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transaction_traces");
            //act.authorization.actor
            //"receipt.receiver", "eosio"
            FindIterable<Document> action_traces = collection.find().filter((Filters.elemMatch("action_traces", Filters.eq("act.authorization.actor", currentAccount)))).sort(Sorts.descending("action_traces.inline_traces.receipt.global_sequence"));
            MongoCursor<Document> iterator = action_traces.iterator();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                recordBean = new ScoreRecordBean();
                recordBean.created_at = document.get("createdAt").toString();
                recordBean.id = document.get("id").toString();
                ArrayList<Document> array = (ArrayList<Document>) document.get("action_traces");
                Document act = (Document) array.get(0).get("act");
                recordBean.source = act.get("name").toString();
                Document data = (Document) act.get("data");
                recordBean.points = data.get("rewards").toString();
                if (recordMap.get(recordBean.id) == null) {  //每条记录会有相同的两条，需 去重
                    e.onNext(recordBean);
                    if (isProgressing) recordBeans.add(recordBean);
                    recordMap.put(recordBean.id, recordBean);
                } else {
                    recordMap.remove(recordBean.id);
                }
            }
            e.onComplete();
        }).buffer(20).compose(RxUtil.switchSchedulers()).subscribeWith(subscriber);
    }

    private ConcurrentHashMap<String, ScoreRecordBean> recordMap = new ConcurrentHashMap<>();


    private void setCurrentAccount(String name) {
        setTitleText(name);
        if (TextUtils.equals(name, currentAccount)) return;
        currentAccount = name;
        obtainAdapter.clearData();
        obtainRecyclerView.showProgressView();
        isProgressing = true;
        totalSize = 0;
        getData();
    }

    private void switchAccount() {
        if (panel == null) {
            List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
            panel = new SimpleChoosePanel.Builder<>(getCtx(), (SimpleChoosePanel.OnSelectedFinished<EosAccount>) account -> {
                setCurrentAccount(account.getName());
            }).setData((ArrayList<EosAccount>) allEosAccount).setTitle("选择账户").build();
        }
        panel.show();
    }

    class ScoreRecordPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (position == 1) {
                consumeRecyclerView = new CarefreeRecyclerView(getCtx());
                consumeRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                consumeRecyclerView.showEmptyView("暂无积分支出");
                container.addView(consumeRecyclerView);
                return consumeRecyclerView;
            } else {
                obtainRecyclerView = new CarefreeRecyclerView(getCtx());
                obtainRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                obtainAdapter = new ScoreRecordAdapter(position);
                obtainRecyclerView.setAdapter(obtainAdapter);
                obtainRecyclerView.showProgressView();
                container.addView(obtainRecyclerView);
                return obtainRecyclerView;
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    protected void onDestroy() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
        super.onDestroy();
    }
}
