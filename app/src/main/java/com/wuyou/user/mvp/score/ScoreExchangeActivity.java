package com.wuyou.user.mvp.score;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by DELL on 2018/9/20.
 */

public class ScoreExchangeActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.score_exchange);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_exchage;
    }
}
