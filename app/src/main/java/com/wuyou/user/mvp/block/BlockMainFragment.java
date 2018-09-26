package com.wuyou.user.mvp.block;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyou.user.R;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockMainFragment extends BaseFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_block;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.block_main_search)
    public void onViewClicked() {
        Intent intent = new Intent(mCtx, BlockSearchActivity.class);
        startActivity(intent);
    }
}
