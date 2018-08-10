package com.wuyou.user.view.widget.pullToResfresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.wuyou.user.R;
import com.wuyou.user.util.JZVideoPlayerFullscreen;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by DELL on 2018/8/10.
 */

public class HomeRefreshLayout extends FrameLayout implements NestedScrollingParent {
    private int NORMAL_STATE = 0;
    private int PULL_STATE = 1;
    private int RELEASE_TO_REFRESH_STATE = 2;
    private int RELEASE_TO_VIDEO_STATE = 3;
    private int RELEASE_VIDEO_STATE = 4;
    private int REFRESHING_STATE = 5;
    private int RELEASE_TO_RESET_STATE = 6;

    private View refreshView;

    public HomeRefreshLayout(Context context) {
        this(context,null);
    }


    public HomeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refreshView = getChildAt(0);
        initRefreshLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MAX_LOADING_LAYOUT_HEIGHT = getMeasuredHeight() / 2;
        refreshView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
        measureHeader(widthMeasureSpec, stateText);
        measureHeader(widthMeasureSpec, video);
    }

    private int MIN_LOADING_LAYOUT_HEIGHT = 40;
    private int MAX_LOADING_LAYOUT_HEIGHT;

    private void measureHeader(int widthMeasureSpec, View view) {
        int height = Math.max(MIN_LOADING_LAYOUT_HEIGHT, view.getMeasuredHeight());
        height = Math.min(height, MAX_LOADING_LAYOUT_HEIGHT);
        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        stateText.layout(0, -stateText.getMeasuredHeight(), stateText.getMeasuredWidth(), 0);
        video.layout(0, -video.getMeasuredState() - stateText.getMeasuredHeight(), video.getMeasuredWidth(), -stateText.getMeasuredHeight());
        refreshView.layout(0, 0, refreshView.getMeasuredWidth(), refreshView.getMeasuredHeight());
    }

    private TextView stateText;
    private JZVideoPlayerStandard video;

    protected void initRefreshLayout() {
        if (stateText != null) removeView(stateText);
        stateText = new TextView(getContext());
        stateText.setVisibility(GONE);
        video = new JZVideoPlayerFullscreen(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, DensityUtils.dip2px(getContext(), 180));
        video.setLayoutParams(params);

        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(-1, DensityUtils.dip2px(getContext(), 20));
        stateText.setGravity(Gravity.CENTER);
        stateText.setTextColor(getContext().getResources().getColor(R.color.common_gray));
        stateText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        stateText.setLayoutParams(tvParams);

        addView(video);
        addView(stateText);
    }
}
