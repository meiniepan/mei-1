package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.bean.WalletBalance;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressManagerActivity;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.InfoActivity;
import com.wuyou.user.view.activity.SettingActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    public void onResume() {
        super.onResume();
        getBalance();
    }

    private void getBalance() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                .getWalletBalance(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletBalance>>() {
                    @Override
                    public void onSuccess(BaseResponse<WalletBalance> walletBalanceBaseResponse) {
                        mineBalance.setText(CommonUtil.formatPrice(walletBalanceBaseResponse.data.balance));
                    }
                });
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
                intent.setClass(mCtx, AddressManagerActivity.class);
                intent.putExtra(Constant.ADDRESS_SOURCE, 1);
                startActivity(intent);
                break;
            case R.id.mine_card:
            case R.id.mine_activity:
            case R.id.mine_recharge:
                ToastUtils.ToastMessage(mCtx, R.string.no_function);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, InfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
