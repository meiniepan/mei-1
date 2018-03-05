package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.SettingActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_head)
    ImageView mineHead;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_sex)
    TextView mineSex;
    @BindView(R.id.mine_phone)
    TextView minePhone;
    @BindView(R.id.mine_setting)
    ImageView mineSetting;
    @BindView(R.id.mine_balance)
    TextView mineBalance;
    @BindView(R.id.mine_recharge)
    TextView mineRecharge;
    @BindView(R.id.activity_record)
    Button rechargeRecord;
    @BindView(R.id.mine_login)
    TextView mineLogin;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        setLoginInfo();
    }

    private void setLoginInfo() {
        UserInfo userInfo = CarefreeApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            mineLogin.setVisibility(View.GONE);
            GlideUtils.loadImage(mCtx, userInfo.getHead_image(), mineHead,true);
            minePhone.setText(userInfo.getMobile());
            mineName.setText(userInfo.getName());
            mineSex.setText(userInfo.getGender());
        } else {
            mineLogin.setVisibility(View.VISIBLE);
            mineHead.setImageResource(R.mipmap.default_pic);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        setLoginInfo();
    }

    @Override
    public void showError(String message, int res) {

    }


    @OnClick(R.id.mine_setting)
    public void onViewClicked() {
        Intent view = new Intent(getContext(), SettingActivity.class);
        startActivity(view);
    }

    @OnClick({R.id.mine_setting, R.id.mine_recharge, R.id.activity_record, R.id.mine_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_setting:
                break;
            case R.id.mine_recharge:
                break;
            case R.id.activity_record:
                break;
            case R.id.mine_login:
                Intent intent = new Intent(mCtx, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
