package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.SettingActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_setting)
    TextView mineSetting;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void showError(String message, int res) {

    }


    @OnClick(R.id.mine_setting)
    public void onViewClicked() {
        Intent view = new Intent(getContext(), SettingActivity.class);
        startActivity(view);
    }
}
