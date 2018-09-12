package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.common.widget.panel.SimpleChoosePanel;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.remote.ScoreRecordBean;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EosAccount mainAccount = CarefreeDaoSession.getInstance().getMainAccount();
        setTitleText(mainAccount.getName());
        setTitleIcon(R.mipmap.score_record_switch, v -> switchAccount());
        scoreRecordPager.setAdapter(new ScoreRecordPagerAdapter());
        scoreRecordTab.setupWithViewPager(scoreRecordPager);

//            scoreRecordList.activeRefresh(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
//                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", "0").buildGet()));
//            scoreRecordList.initData(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
//                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", "0").buildGet()));
//            scoreRecordList.setLoadMoreListener(() -> scoreRecordList.getDataMore(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
//                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", scoreRecordList.startId).put("flag", "2").buildGet())));
    }

    private void switchAccount() {
        if (panel == null) {
            List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
            panel = new SimpleChoosePanel.Builder<>(getCtx(), (SimpleChoosePanel.OnSelectedFinished<EosAccount>) account -> setTitleText(account.getName())).setData((ArrayList<EosAccount>) allEosAccount).setTitle("选择账户").build();
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
            CarefreeRecyclerView recyclerView = new CarefreeRecyclerView(getCtx());
            recyclerView.setEmptyIcon(R.mipmap.empty_score);
            ScoreRecordAdapter adapter = new ScoreRecordAdapter(position);
            recyclerView.setAdapter(adapter);
            if (position==1){
                recyclerView.showEmptyView("暂无积分支出");
            }else {
                recyclerView.showContentView();
                //TODO
                ArrayList<ScoreRecordBean> data = new ArrayList<>();
                data.add(new ScoreRecordBean());
                data.add(new ScoreRecordBean());
                data.add(new ScoreRecordBean());
                adapter.setNewData(data);
            }
            container.addView(recyclerView);
            return recyclerView;
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
}
