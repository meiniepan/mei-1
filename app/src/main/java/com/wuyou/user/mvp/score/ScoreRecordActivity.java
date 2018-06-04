package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Solang on 2018/6/1.
 */

public class ScoreRecordActivity extends BaseActivity {
    @BindView(R.id.score_record_title)
    TextView scoreRecordTitle;
    @BindView(R.id.score_record_flag)
    TextView scoreRecordFlag;
    @BindView(R.id.score_amount)
    TextView scoreAmount;
    @BindView(R.id.score_record_list)
    RefreshRecyclerView scoreRecordList;
    private int flag;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra(Constant.SCORE_FLAG, 0);
        scoreRecordList.setAdapter(new ScoreRecordAdapter(R.layout.item_score_record));
        scoreRecordList.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));

        if (flag == 0) {
            scoreRecordTitle.setText(R.string.gained_score_record);
            scoreRecordFlag.setText(R.string.total_gained_score);
        } else {
            scoreRecordTitle.setText(R.string.consume_score_record);
            scoreRecordFlag.setText(R.string.total_consume_score);
        }

        scoreRecordList.showEmptyView("暂无积分记录");
    }
}
