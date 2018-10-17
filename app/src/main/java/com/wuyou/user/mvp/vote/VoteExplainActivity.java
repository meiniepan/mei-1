package com.wuyou.user.mvp.vote;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by Solang on 2018/10/15.
 */

public class VoteExplainActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_vote_explain;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.vote_explain));

    }
}
