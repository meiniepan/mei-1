package com.wuyou.user.view.widget.pullToResfresh;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.R;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.bean.ShareBean;
import com.wuyou.user.util.JZVideoPlayerFullscreen;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.widget.panel.ShareBottomBoard;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;


/**
 * Created by yan on 2017/9/16.
 */

public class HomeVideoHeader extends PullRefreshView implements JZVideoPlayerFullscreen.OnShareListener {
    private final String TWO_REFRESH_TEXT = "松开看视频";
    private final int REFRESH_FIRST_DURING = 180;
    private final int TWO_REFRESH_DURING = 400;

    private int twoRefreshDistance;
    private int firstRefreshTriggerDistance;

    private PullRefreshLayout pullRefreshLayout;
    public boolean isShowVideo;
    private ValueAnimator translateYAnimation;
    private JZVideoPlayerFullscreen video;

    public boolean isShowVideo() {
        return isShowVideo;
    }

    private Activity activity;

    public HomeVideoHeader(Activity context, PullRefreshLayout pullRefreshLayout) {
        super(context);
        activity = context;
        this.pullRefreshLayout = pullRefreshLayout;
        twoRefreshInit();
    }

    protected TextView tv;
    protected LinearLayout rlContainer;

    private void twoRefreshInit() {
        setLayoutParams(new ViewGroup.LayoutParams(-1, DensityUtils.dip2px(getContext(), 200)));
        firstRefreshTriggerDistance = HomeVideoHeader.this.pullRefreshLayout.getRefreshTriggerDistance();
        twoRefreshDistance = firstRefreshTriggerDistance + DensityUtils.dip2px(getContext(), 180);

        View view = LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        rlContainer = view.findViewById(R.id.rl_container);
        tv = new TextView(getContext());
        tv.setVisibility(GONE);
        video = new JZVideoPlayerFullscreen(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, DensityUtils.dip2px(getContext(), 180));
        video.setLayoutParams(params);
        rlContainer.addView(video);

        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(-1, DensityUtils.dip2px(getContext(), 20));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getContext().getResources().getColor(R.color.common_gray));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        tv.setLayoutParams(tvParams);
        rlContainer.addView(tv);
        animationInit();
    }

    @Override
    protected int contentView() {
        return R.layout.refresh_view;
    }

    private void animationInit() {
        translateYAnimation = ValueAnimator.ofFloat(0, 0);
        translateYAnimation.setDuration(TWO_REFRESH_DURING);
        translateYAnimation.setInterpolator(new ViscousInterpolator());
        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float m = (float) animation.getAnimatedValue();
                pullRefreshLayout.moveChildren((int) m);
            }
        });
    }

    private float lastY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isShowVideo) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                if (translateYAnimation.isRunning()) {
                    translateYAnimation.cancel();
                }
                if (pullRefreshLayout.getMoveDistance() == getHeight() && lastY - nowY > 200) {
                    pullRefreshLayout.refreshComplete();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (pullRefreshLayout.getMoveDistance() <= getHeight() - firstRefreshTriggerDistance) {
                    pullRefreshLayout.refreshComplete();
                } else if (pullRefreshLayout.getMoveDistance() < getHeight()) {
                    translateYAnimation.setFloatValues(pullRefreshLayout.getMoveDistance(), getHeight());
                    translateYAnimation.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPullChange(float percent) {
        super.onPullChange(percent);
        if(pullRefreshLayout.getMoveDistance()==0){
            tv.setVisibility(VISIBLE);
        }
        if (!pullRefreshLayout.isHoldingTrigger()) {
            if (pullRefreshLayout.getMoveDistance() >= twoRefreshDistance) {
                if (!tv.getText().toString().equals(TWO_REFRESH_TEXT)) {
                    tv.setText(TWO_REFRESH_TEXT);
                }
            } else if (pullRefreshLayout.getMoveDistance() > firstRefreshTriggerDistance){
                tv.setText("松开刷新");
            }else {
                tv.setText("下拉刷新");
            }
        } else if (pullRefreshLayout.getMoveDistance() > getHeight() + 50) {
            pullRefreshLayout.setDispatchPullTouchAble(true);
            isShowVideo = true;
        }
    }

    @Override
    public void onPullHolding() {
        tv.setText("刷新中");
        if (pullRefreshLayout.getMoveDistance() >= twoRefreshDistance) {
            pullRefreshLayout.setPullDownMaxDistance(getHeight() * 2);
            pullRefreshLayout.setRefreshTriggerDistance(getHeight());
            pullRefreshLayout.setRefreshAnimationDuring(TWO_REFRESH_DURING);
            pullRefreshLayout.setDispatchPullTouchAble(false);
            pullRefreshLayout.setTwinkEnable(false);
        }
    }

    @Override
    public void onPullFinish(boolean flag) {
        tv.setText("刷新完成");
        if (isShowVideo) {
            isShowVideo = false;
            pullRefreshLayout.setTwinkEnable(true);
            pullRefreshLayout.setPullDownMaxDistance(getHeight() * 2);
            pullRefreshLayout.setRefreshTriggerDistance(firstRefreshTriggerDistance);
            pullRefreshLayout.setRefreshAnimationDuring(REFRESH_FIRST_DURING);
            pullRefreshLayout.setDispatchPullTouchAble(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (translateYAnimation.isRunning()) {
            translateYAnimation.cancel();
        }
    }

    @Override
    public void onPullHoldTrigger() {
        tv.setText("松开刷新");
    }

    //手动推回去
    @Override
    public void onPullHoldUnTrigger() {
        tv.setText("下拉刷新 ");
    }

    @Override
    public void onPullReset() {
        tv.setText("下拉刷新");
    }

    public void setRefreshFinishText() {
        tv.setText("刷新完成");
    }


    private HomeVideoBean homeVideoBean;

    public void setVideoData(List<HomeVideoBean> videoData) {
        if (homeVideoBean == null) {

        }
        if (videoData != null && videoData.size() > 1) {
            homeVideoBean = videoData.get(0);
            video.setUp(homeVideoBean.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean.title);
            GlideUtils.loadImage(getContext(), homeVideoBean.preview, video.thumbImageView);
            video.addShareListener(this);
        } else {
            ToastUtils.ToastMessage(getContext(), "获取视频信息失败" + videoData);
        }
    }

    @Override
    public void onShare(String url, int platform) {
        ShareBottomBoard bottomBoard = new ShareBottomBoard(getContext());
        if (TextUtils.equals(url, homeVideoBean.video)) {
            bottomBoard.setData(copyToShare(homeVideoBean));
        }
        bottomBoard.show();
        bottomBoard.setOnDismissListener(dialog -> activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
    }

    private ShareBean copyToShare(HomeVideoBean homeVideoBean) {
        ShareBean shareBean = new ShareBean();
        shareBean.targetUrl = homeVideoBean.video;
        shareBean.miniPath = ""; //TODO
        shareBean.miniType = 0;
        shareBean.preview = homeVideoBean.preview;
        shareBean.summary = homeVideoBean.summary;
        shareBean.title = homeVideoBean.title;
        return shareBean;
    }
}