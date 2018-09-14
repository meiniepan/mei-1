package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.data.remote.WalletBalance;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressManagerActivity;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.mvp.score.ScoreRecordActivity;
import com.wuyou.user.mvp.score.SignInActivity;
import com.wuyou.user.mvp.wallet.ScoreAccountActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HelpActivity;
import com.wuyou.user.view.activity.InfoActivity;
import com.wuyou.user.view.activity.SettingActivity;
import com.wuyou.user.view.activity.WebActivity;
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
    @BindView(R.id.mine_login)
    TextView mineLogin;
    private long totalScore = -1;

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
            setInfo(userInfo);
        } else {
            mineLogin.setVisibility(View.VISIBLE);
            mineHead.setImageResource(R.mipmap.default_pic);
        }
    }

    private void setInfo(UserInfo userInfo) {
        if (!TextUtils.isEmpty(userInfo.getAvatar()))
            GlideUtils.loadImageNoHolder(mCtx, CarefreeDaoSession.getAvatar(userInfo), mineHead, true);
        minePhone.setText(userInfo.getMobile());
        mineName.setText(userInfo.getName());
        totalScore = userInfo.getReceived_points();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        setLoginInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CarefreeDaoSession.getInstance().getUserInfo() == null) return;
        getBalance();
        getInfo();
    }

    public void getInfo() {
        CarefreeRetrofit.getInstance().createApi(UserApis.class).getUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .doOnNext(userInfoBaseResponse -> {
                    UserInfo info = userInfoBaseResponse.data;
                    info.setToken(CarefreeDaoSession.getInstance().getUserInfo().getToken());
                    info.setMid(CarefreeDaoSession.getInstance().getUserInfo().getMid());
                    CarefreeDaoSession.getInstance().updateUserInfo(info);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        setInfo(userInfoBaseResponse.data);
                    }
                });
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

    @OnClick({R.id.mine_setting, R.id.mine_warn, R.id.mine_login, R.id.mine_card, R.id.mine_address, R.id.mine_activity, R.id.mine_info, R.id.mine_score, R.id.mine_sign_in, R.id.mine_help})
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
            case R.id.mine_activity:
                if (CommonUtil.checkNetworkNoConnected(mCtx)) return;
                intent.setClass(mCtx, WebActivity.class);
                intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL + "activity_annal?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken());
                startActivity(intent);
                break;
            case R.id.mine_card:
                ToastUtils.ToastMessage(mCtx, R.string.no_function);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_score:
                //todo
                intent.setClass(mCtx, ScoreAccountActivity.class);
                startActivity(intent);
//                if (totalScore == -1) {
//                    ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
//                    return;
//                }
//                intent.setClass(mCtx, ScoreActivity.class);
//                startActivity(intent);
                break;
//            case R.id.mine_scan:
//                if (askForPermissions(Manifest.permission.CAMERA)) {
//                    intent.setClass(mCtx, CaptureActivity.class);
//                    startActivity(intent);
//                }
//                break;
            case R.id.mine_sign_in:
                intent.setClass(mCtx, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_warn:
                new CustomAlertDialog.Builder(mCtx).setTitle(R.string.prompt).setMessage(R.string.mine_warn)
                        .setPositiveButton("确定", (dialog, which) -> {
                        }).create().show();
                break;
            case R.id.mine_help:
                intent.setClass(mCtx, HelpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
