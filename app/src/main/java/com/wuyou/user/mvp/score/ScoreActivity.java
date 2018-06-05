package com.wuyou.user.mvp.score;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.RadarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/6/1.
 */

public class ScoreActivity extends BaseActivity {
    @BindView(R.id.score_obtain)
    LinearLayout scoreObtain;
    @BindView(R.id.score_consumed)
    LinearLayout scoreConsumed;

    @Override
    protected void bindView(Bundle savedInstanceState) {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score;
    }


    @OnClick({R.id.score_obtain, R.id.score_consumed})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getCtx(), ScoreRecordActivity.class);
        switch (view.getId()) {
            case R.id.score_obtain:
                intent.putExtra(Constant.SCORE_FLAG, 0);
                startActivity(intent);
                break;
            case R.id.score_consumed:
                intent.putExtra(Constant.SCORE_FLAG, 1);
                startActivity(intent);
                break;
        }
    }
}
