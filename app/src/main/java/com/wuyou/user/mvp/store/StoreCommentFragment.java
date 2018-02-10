package com.wuyou.user.mvp.store;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.view.fragment.BaseFragment;

/**
 * Created by hjn on 2018/2/10.
 */

public class StoreCommentFragment extends BaseFragment {
    @Override
    public void showError(String message, int res) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store_comment;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }
}
