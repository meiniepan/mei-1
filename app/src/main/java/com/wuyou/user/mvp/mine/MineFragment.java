package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressActivity;
import com.wuyou.user.mvp.address.AddressManagerActivity;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.InfoActivity;
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
    @BindView(R.id.mine_balance)
    TextView mineBalance;
    @BindView(R.id.mine_recharge)
    TextView mineRecharge;
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
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
        if (userInfo != null) {
            mineLogin.setVisibility(View.GONE);
//            GlideUtils.loadImage(mCtx, userInfo.getHead_image(), mineHead,true);
            minePhone.setText(userInfo.getMobile());
//            mineName.setText(userInfo.getName());
//            mineSex.setText(userInfo.getGender());
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


    @OnClick({R.id.mine_setting, R.id.mine_recharge, R.id.mine_login, R.id.mine_card, R.id.mine_address, R.id.mine_activity, R.id.mine_info})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_setting:
                intent.setClass(mCtx, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_login:
                intent.setClass(mCtx, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_address:
                intent.setClass(mCtx, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_card:
            case R.id.mine_activity:
            case R.id.mine_recharge:
                ToastUtils.ToastMessage(mCtx,R.string.no_function);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, InfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
