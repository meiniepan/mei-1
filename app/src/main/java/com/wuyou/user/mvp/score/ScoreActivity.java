package com.wuyou.user.mvp.score;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.CaptureActivity;
import com.wuyou.user.view.activity.MainActivity;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/6/1.
 */

public class ScoreActivity extends BaseActivity {

    @BindView(R.id.text_day_1)
    TextView textDay1;
    @BindView(R.id.text_day_2)
    TextView textDay2;
    @BindView(R.id.text_day_3)
    TextView textDay3;
    @BindView(R.id.text_day_4)
    TextView textDay4;
    @BindView(R.id.text_day_5)
    TextView textDay5;
    @BindView(R.id.text_day_6)
    TextView textDay6;
    @BindView(R.id.text_day_7)
    TextView textDay7;
    @BindView(R.id.score_sign_up)
    TextView scoreSignUp;
    @BindView(R.id.sign_record_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.score_sign_success)
    TextView scoreSignSuccess;

    private int signRecord;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.mine_score);
        signRecord = SharePreferenceManager.getInstance(getCtx()).getIntValue(Constant.SIGN_UP_RECORD, -1);
        long lastSignTime = SharePreferenceManager.getInstance(getCtx()).getLongValue(Constant.LAST_SIGN_TIME);
        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(new Date(lastSignTime));
        int lastDate = calendar.get(Calendar.DAY_OF_YEAR);
        if (signRecord != -1 && signRecord != 7) {
            for (int i = 0; i <= signRecord; i++) {
                TextView textView = (TextView) constraintLayout.getChildAt(i);
                textView.setBackgroundResource(R.drawable.blue_circle_bg);
            }
        }
        if (currentDate == lastDate) { //今天签到了
            setAlreadySignStatus();
        } else { //新的一天 展示全部未签到状态
            setEnableSignStatus();
            if (signRecord == 7) {
                signRecord = -1;
            }
        }
    }

    private void setEnableSignStatus() {
        if (signRecord != -1 && signRecord != 7) {
            TextView textView = (TextView) constraintLayout.getChildAt(signRecord + 1);
            textView.setText("");
            textView.animate().scaleY(1.3f).scaleX(1.3f).setDuration(0).start();
            textView.setBackgroundResource(R.mipmap.sign_today);
        }
    }

    private void setAlreadySignStatus() {
        scoreSignUp.setEnabled(false);
        scoreSignUp.setText(R.string.already_sign_up);
        scoreSignUp.setTextColor(getResources().getColor(R.color.common_gray));
        if (signRecord != -1 && signRecord != 7) {
            TextView textView = (TextView) constraintLayout.getChildAt(signRecord);
            textView.setText("");
            textView.animate().scaleY(1.3f).scaleX(1.3f).setDuration(0).start();
            textView.setBackgroundResource(R.mipmap.signed_today);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score;
    }

    private void signUp() {
        showLoadingDialog();
        EoscDataManager.getIns().getDailyRewords().compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        signUpSuccess();
                    }
                });
    }

    private void signUpSuccess() {
        setAlreadySignStatus();
        SharePreferenceManager.getInstance(getCtx()).setValue(Constant.LAST_SIGN_TIME, System.currentTimeMillis());
        signRecord += 1;
        SharePreferenceManager.getInstance(getCtx()).setValue(Constant.SIGN_UP_RECORD, signRecord);
        startScoreAnimation();
    }

    private void startScoreAnimation() {
        scoreSignSuccess.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0);
        TranslateAnimation translateAnimation = new TranslateAnimation(scoreSignSuccess.getX(), scoreSignSuccess.getX(), scoreSignSuccess.getY() - DensityUtils.dip2px(getCtx(), 40),
                scoreSignSuccess.getY() - DensityUtils.dip2px(getCtx(), 71));
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(2000);
        scoreSignSuccess.setAnimation(animationSet);
        animationSet.setFillAfter(true);
        animationSet.start();
    }

    @OnClick({R.id.score_action_1, R.id.score_action_2, R.id.score_action_3, R.id.score_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.score_action_1:
                Intent intent = new Intent(getCtx(), MainActivity.class);
                intent.putExtra(Constant.MAIN_FLAG, 2);
                startActivity(intent);
                break;
            case R.id.score_action_2:
                Intent intent1 = new Intent(getCtx(), CaptureActivity.class);
                startActivity(intent1);
                break;
            case R.id.score_action_3:
                break;
            case R.id.score_sign_up:
                signUp();
                break;
        }
    }
}
