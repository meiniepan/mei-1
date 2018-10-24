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
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.data.remote.WalletBalance;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressManagerActivity;
import com.wuyou.user.mvp.block.BlockExplorerActivity;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.mvp.score.ScoreExchangeActivity;
import com.wuyou.user.mvp.score.ScoreMissionActivity;
import com.wuyou.user.mvp.trace.TraceAuthActivity;
import com.wuyou.user.mvp.trace.TracePresenter;
import com.wuyou.user.mvp.vote.VoteActivity;
import com.wuyou.user.mvp.wallet.ActivityRecordActivity;
import com.wuyou.user.mvp.wallet.CreateOrImportAccountActivity;
import com.wuyou.user.mvp.wallet.ScoreAccountActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HelpActivity;
import com.wuyou.user.view.activity.InfoActivity;
import com.wuyou.user.view.activity.MainActivity;
import com.wuyou.user.view.activity.SettingActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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

    private void getBalance() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                .getWalletBalance(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletBalance>>() {
                    @Override
                    public void onSuccess(BaseResponse<WalletBalance> walletBalanceBaseResponse) {
//                        mineBalance.setText(CommonUtil.formatPrice(walletBalanceBaseResponse.data.balance));
                    }
                });
    }

    @OnClick({R.id.mine_setting, R.id.mine_login, R.id.mine_address, R.id.mine_activity, R.id.mine_info, R.id.mine_score, R.id.mine_help,
            R.id.mine_mission, R.id.mine_auth, R.id.mine_explorer, R.id.mine_vote, R.id.mine_trace, R.id.mine_kyc})
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
//                ArrayList<String> pictureHashList = new ArrayList<>();
//                pictureHashList.add("QmeWNdnA6nY6BdABdL9BcD2ACCkKi7QmTqRycJ55VQ417v");
//                pictureHashList.add("QmZFm3TxcXb1pieN4itmAdFdjyRxo8jym2hgrzSnbsRemt");
//                pictureHashList.add("QmadipibuuLFd9SD6QBTsu946jvo4nMGgLNLn2SmWKzoK3");
//                new TracePresenter().uploadTrace("hahahahahahahah", pictureHashList, 10);

                new TracePresenter().getProposalTable();
                new TracePresenter().getApproveTable();
                break;
            case R.id.mine_trace:
                startActivity(new Intent(getContext(), TraceAuthActivity.class));
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
}
