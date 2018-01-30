package com.wuyou.user.mvp.order;

import android.os.Bundle;

import com.wuyou.user.view.fragment.BaseFragment;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderStatusFragment extends BaseFragment {
    private int type;

    @Override
    public void showError(int res) {

    }

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    public void setType(int type) {
        this.type = type;
    }
}
