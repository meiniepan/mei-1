package com.wuyou.user.mvp.score;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/6/1.
 */

public class ScoreActivity extends BaseActivity {
    @BindView(R.id.score_available)
    TextView scoreAvailable;
    @BindView(R.id.score_obtain_text)
    TextView scoreObtainText;
    @BindView(R.id.score_consumed_text)
    TextView scoreConsumedText;
    private long consumeScore;
    private long totalScore;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.mine_score);
        baseStatusLayout.setErrorAction(v -> getInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    public void getInfo() {
        baseStatusLayout.showProgressView();
        CarefreeRetrofit.getInstance().createApi(UserApis.class).getUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        baseStatusLayout.showContentView();

                        totalScore = userInfoBaseResponse.data.getReceived_points();
                        consumeScore = userInfoBaseResponse.data.getOut_points();
                        long availableScore = totalScore - consumeScore;
                        scoreAvailable.setText(availableScore + "");
                        scoreObtainText.setText(totalScore + "");
                        scoreConsumedText.setText(consumeScore + "");
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        baseStatusLayout.showErrorView();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score;
    }


    @OnClick({R.id.score_obtain, R.id.score_consumed})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.score_obtain:
                intent.setClass(getCtx(), ScoreRecordActivity.class);
                intent.putExtra(Constant.SCORE_FLAG, 0);
                intent.putExtra(Constant.SCORE_AMOUNT, totalScore);
                startActivity(intent);
                break;
            case R.id.score_consumed:
                intent.setClass(getCtx(), ScoreRecordActivity.class);
                intent.putExtra(Constant.SCORE_FLAG, 1);
                intent.putExtra(Constant.SCORE_AMOUNT, consumeScore);
                startActivity(intent);
                break;
//            case R.id.score_sign:
//                intent.setClass(getCtx(), SignInActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.score_scan:
//                intent.setClass(getCtx(), CaptureActivity.class);
//                startActivity(intent);
//                break;
        }
    }
}
