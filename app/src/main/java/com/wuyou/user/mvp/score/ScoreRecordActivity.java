package com.wuyou.user.mvp.score;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import butterknife.BindView;

/**
 * Created by Solang on 2018/6/1.
 */

public class ScoreRecordActivity extends BaseActivity {
    @BindView(R.id.score_record_flag)
    TextView scoreRecordFlag;
    @BindView(R.id.score_amount)
    TextView scoreAmount;
    @BindView(R.id.score_record_list)
    CarefreeRecyclerView scoreRecordList;
    private int flag;
    private ScoreRecordAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindView(Bundle savedInstanceState) {
        scoreRecordList.getRecyclerView().setEmptyContentViewMargin(0, -200, 0, 0);
        flag = getIntent().getIntExtra(Constant.SCORE_FLAG, 0);
        adapter = new ScoreRecordAdapter(R.layout.item_score_record, flag);
        scoreRecordList.setAdapter(adapter);
        if (flag == 0) {
            setTitleText(R.string.gained_score_record);
            scoreRecordFlag.setText(R.string.total_gained_score);
            scoreRecordList.activeRefresh(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", "0").buildGet()));
            scoreRecordList.initData(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", "0").buildGet()));
            scoreRecordList.setLoadMoreListener(() -> scoreRecordList.getDataMore(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                    .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", scoreRecordList.startId).put("flag", "2").buildGet())));
        } else {
            setTitleText(R.string.consume_score_record);
            scoreRecordFlag.setText(R.string.total_consume_score);
            scoreRecordList.showEmptyView("暂无消耗积分记录");
        }
        scoreAmount.setText(getIntent().getLongExtra(Constant.SCORE_AMOUNT, 0) + "");
    }
}
