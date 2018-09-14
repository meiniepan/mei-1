package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.remote.ScoreRecordBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
        EosAccount mainAccount = CarefreeDaoSession.getInstance().getMainAccount();
        setTitleIcon(R.mipmap.score_record_switch, v -> switchAccount());
        scoreRecordPager.setAdapter(new ScoreRecordPagerAdapter());
        scoreRecordTab.setupWithViewPager(scoreRecordPager);

        mongoClient = MongoClients.create(Constant.EOS_MONGO_DB);
        setCurrentAccount(mainAccount.getName());
    }

    private boolean isProgressing = true;

    private void getData() {
        subscriber = new BaseSubscriber<List<ScoreRecordBean>>() {
            @Override
            public void onSuccess(List<ScoreRecordBean> data) {
                if (data.size() != 0) {
                    if (isProgressing) {
                        obtainRecyclerView.showContentView();
                    }
                    obtainAdapter.addData(data);
                    recordBeans.clear();
                    isProgressing = false;
                }
            }

            @Override
            public void onComplete() {
                if (isProgressing) {
                    isProgressing = false;
                    obtainRecyclerView.showContentView();
                }
                obtainAdapter.addData(recordBeans);
            }
        };
        Observable.create((ObservableOnSubscribe<ScoreRecordBean>) e -> {
            MongoDatabase database = mongoClient.getDatabase("EOS");
            MongoCollection<Document> collection = database.getCollection("transaction_traces");
            FindIterable<Document> documents = collection.find();
            FindIterable<Document> action_traces = documents.filter(Filters.elemMatch("action_traces", Filters.eq("act.authorization.actor", currentAccount)));
            MongoCursor<Document> iterator = action_traces.iterator();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                recordBean = new ScoreRecordBean();
                recordBean.created_at = document.get("createdAt").toString();
                ArrayList<Document> array = (ArrayList<Document>) document.get("action_traces");
                Document act = (Document) array.get(0).get("act");
                recordBean.source = act.get("name").toString();
                Document data = (Document) act.get("data");
                recordBean.points = data.get("rewards").toString();
                e.onNext(recordBean);
                if (isProgressing) recordBeans.add(recordBean);
            }
            e.onComplete();
        }).buffer(10).compose(RxUtil.switchSchedulers()).subscribeWith(subscriber);
    }

    private void setCurrentAccount(String name) {
        setTitleText(name);
        if (TextUtils.equals(name, currentAccount)) return;
        currentAccount = name;
        getData();
    }

    private void switchAccount() {
        if (panel == null) {
            List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
            panel = new SimpleChoosePanel.Builder<>(getCtx(), (SimpleChoosePanel.OnSelectedFinished<EosAccount>) account -> {
                subscriber.dispose();
                setCurrentAccount(account.getName());
                obtainAdapter.clearData();
                obtainRecyclerView.showProgressView();
                isProgressing = true;
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
        super.onDestroy();
        if (subscriber != null) subscriber.dispose();
    }
}
