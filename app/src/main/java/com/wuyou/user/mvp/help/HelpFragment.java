package com.wuyou.user.mvp.help;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.view.fragment.BaseFragment;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HelpFragment extends BaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_help;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void showError(String message, int res) {

    }
}
