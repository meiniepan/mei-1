package com.wuyou.user.view.widget.pullToResfresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuyou.user.R;


/**
 * Created by yan on 2017/7/4.
 */

public class HomeVideoHeader extends PullRefreshView {
    protected TextView tv;
    protected LinearLayout rlContainer;

    private boolean isStateFinish;
    private boolean isHolding;

    private PullRefreshLayout pullRefreshLayout;

    public HomeVideoHeader(Context context, PullRefreshLayout pullRefreshLayout) {
        super(context);
        this.pullRefreshLayout = pullRefreshLayout;
        View view = LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        rlContainer = view.findViewById(R.id.rl_container);
        tv = view.findViewById(R.id.main_head_title);
        tv.setText("下拉刷新");
    }



    @Override
    protected int contentView() {
        return R.layout.refresh_view;
    }

    @Override
    public void onPullChange(float percent) {
        super.onPullChange(percent);
        if (isStateFinish || isHolding) return;
        percent = Math.abs(percent);
        Log.e("Carefree", "onPullChange: " + percent + "......." + pullRefreshLayout.getMoveDistance() + "..........." + pullRefreshLayout.getRefreshTriggerDistance()+"......"+pullRefreshLayout.isHoldingTrigger());
        if (percent > 0.2 && percent < 1) {
//            if (loadingView.getVisibility() != VISIBLE) {
//                loadingView.smoothToShow();
//            }
//            if (percent < 1) {
//                loadingView.setScaleX(percent);
//                loadingView.setScaleY(percent);
//            }
//        } else if (percent <= 0.2 && loadingView.getVisibility() == VISIBLE) {
//            loadingView.smoothToHide();
//        } else if (loadingView.getScaleX() != 1) {
//            loadingView.setScaleX(1f);
//            loadingView.setScaleY(1f);
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

    //刷新
    @Override
    public void onPullHolding() {
        isHolding = true;
        tv.setText("刷新ing....");
    }

    @Override
    public void onPullFinish(boolean flag) {
        tv.setText("刷新完成");
        isStateFinish = true;
    }

    @Override
    public void onPullReset() {
        super.onPullReset();
        tv.setText("下拉刷新");
        isStateFinish = false;
        isHolding = false;
    }
}