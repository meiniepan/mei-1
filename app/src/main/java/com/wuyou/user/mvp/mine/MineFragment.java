package com.wuyou.user.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.AuthTokenResponse;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressManagerActivity;
import com.wuyou.user.mvp.block.BlockExplorerActivity;
import com.wuyou.user.mvp.kyc.KycAuthActivity;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.mvp.score.ScoreExchangeActivity;
import com.wuyou.user.mvp.score.ScoreMissionActivity;
import com.wuyou.user.mvp.trace.TraceAuthActivity;
import com.wuyou.user.mvp.volunteer.TimeBankMainActivity;
import com.wuyou.user.mvp.vote.VoteActivity;
import com.wuyou.user.mvp.wallet.ActivityRecordActivity;
import com.wuyou.user.mvp.wallet.CreateOrImportAccountActivity;
import com.wuyou.user.mvp.wallet.ScoreAccountActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HelpActivity;
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


    @OnClick({R.id.mine_setting, R.id.mine_login, R.id.mine_address, R.id.mine_activity, R.id.mine_info, R.id.mine_score, R.id.mine_help,
            R.id.mine_mission, R.id.mine_auth, R.id.mine_explorer, R.id.mine_vote, R.id.mine_trace, R.id.mine_kyc, R.id.mine_time_bank})
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
                intent.setClass(mCtx, ActivityRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_score:
                checkDbAndAccount(intent, ScoreAccountActivity.class);
                break;
            case R.id.mine_help:
                intent.setClass(mCtx, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_mission:
                checkDbAndAccount(intent, ScoreMissionActivity.class);
                break;
            case R.id.mine_auth:
                checkDbAndAccount(intent, ScoreExchangeActivity.class);
                break;
            case R.id.mine_explorer:
                checkDbAndAccount(intent, BlockExplorerActivity.class);
                break;
            case R.id.mine_vote:
                checkDbAndAccount(intent, VoteActivity.class);
                break;
            case R.id.mine_kyc:
                navigateAuth();
                break;
            case R.id.mine_trace:
                checkDbAndAccount(intent, TraceAuthActivity.class);
                break;
            case R.id.mine_time_bank:
                checkDbAndAccount(intent, TimeBankMainActivity.class);
                break;
        }
    }

    private void checkDbAndAccount(Intent intent, Class activity) {
        if (Constant.DEFAULT_DB_NAME.equals(CarefreeDaoSession.getInstance().getDatabaseFormName())) {
            ToastUtils.ToastMessage(mCtx, getString(R.string.check_db_to_update));
            EventBus.getDefault().post(new TokenEvent());
            return;
        }
        if (CarefreeDaoSession.getInstance().getMainAccount() == null) {
            intent.setClass(mCtx, CreateOrImportAccountActivity.class);
            startActivity(intent);
        } else {
            intent.setClass(mCtx, activity);
            startActivity(intent);
        }
    }

    private void navigateAuth() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class).getAuthToken(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<AuthTokenResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AuthTokenResponse> authTokenResponse) {
                        RPSDK.start(authTokenResponse.data.token, getContext(), audit -> setAuthResult(audit));
                    }
                });
    }

    private void setAuthResult(RPSDK.AUDIT audit) {
        Log.e("Carefree", "setAuthResult: " + audit);
        if (audit == RPSDK.AUDIT.AUDIT_PASS) {//认证通过
            checkAuthStatus();
        } else if (audit == RPSDK.AUDIT.AUDIT_FAIL || audit == RPSDK.AUDIT.AUDIT_EXCEPTION) { //认证不通过
            ToastUtils.ToastMessage(mCtx, getString(R.string.auth_fail));
        } else if (audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。
            ToastUtils.ToastMessage(mCtx, getString(R.string.auth_ing));
        }
    }

    public void checkAuthStatus() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class).getAuthStatus(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<AuthTokenResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AuthTokenResponse> authTokenResponseBaseResponse) {
                        int status = authTokenResponseBaseResponse.data.status;
                        if (status == 1) {
                            Intent intent = new Intent(mCtx, KycAuthActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.ToastMessage(mCtx, getString(R.string.auth_fail));
                        }
                    }
                });
    }
}
