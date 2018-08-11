package com.wuyou.user.view.widget.pullToResfresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.wuyou.user.view.widget.InterceptNestedScrollView;

/**
 * Created by DELL on 2018/8/10.
 */

public class HomeRefreshLayout extends LinearLayout implements NestedScrollingParent {
    private final int MAX_DURATION = 2000;
    private int OVERSCROLL_RANGE = 200;


    private int NORMAL_STATE = 0;
    private int PULL_STATE = 1;  //下拉状态
    private int RELEASE_TO_REFRESH_STATE = 2; //松手刷新
    private int RELEASE_TO_VIDEO_STATE = 3;//松手看视频
    private int VIDEO_STATE = 4; //视频中
    private int REFRESHING_STATE = 5; //刷新中
    private int RELEASE_TO_RESET_STATE = 6; //松手回原状态


    private int currentState = VIDEO_STATE;
    private InterceptNestedScrollView refreshView;
    private OverScroller scroller;

    private boolean isRelease = false;
    private boolean isSmoothScrolling = false;

    private int MAX_LOADING_LAYOUT_HEIGHT;
    private int VIDEO_ACTION_LINE;

    public HomeRefreshLayout(Context context) {
        this(context, null);
    }

    public HomeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new OverScroller(context);
        VIDEO_ACTION_LINE = DensityUtils.dip2px(getContext(), 100);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        video = getChildAt(0);
        stateText = (TextView) getChildAt(1);
        refreshView = (InterceptNestedScrollView) getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        refreshView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
        MAX_LOADING_LAYOUT_HEIGHT = getMeasuredHeight();
    }

    private TextView stateText;
    private View video;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (currentState == REFRESHING_STATE) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        isRelease = false;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        isPullHeader = false;
        return true;
    }

    private int getTotalHeight() {
        return stateText.getMeasuredHeight() + video.getMeasuredHeight();
    }

    /**
     * when release from XRefreshLayout!
     *
     * @param child
     */
    @Override
    public void onStopNestedScroll(View child) {
        isRelease = true;
        Log.e("Carefree", "onStopNestedScroll: " + getScrollY() + "......." + getTotalHeight());
        if (getScrollY() < 0) {
            smoothScroll(-getScrollY());
        } else if (getScrollY() > getTotalHeight()) {
            smoothScroll(getTotalHeight() - getScrollY());
        } else if (getScrollY() >= getRefreshHeight() && currentState == RELEASE_TO_RESET_STATE) {
            smoothScroll(getTotalHeight() - getScrollY());
            currentState = NORMAL_STATE;
        } else if ((getScrollY() >= VIDEO_ACTION_LINE && currentState == VIDEO_STATE) || currentState == RELEASE_TO_RESET_STATE) {
            smoothScroll(getTotalHeight() - getScrollY());
            currentState = NORMAL_STATE;
        } else if (getScrollY() >= VIDEO_ACTION_LINE && currentState == RELEASE_TO_REFRESH_STATE && listener != null) {
            listener.onRefresh();
            stateText.setText("刷新中");
            currentState = REFRESHING_STATE;
            smoothScroll(getRefreshHeight() - getScrollY());
        } else if (getScrollY() > 0 && getScrollY() < VIDEO_ACTION_LINE && currentState == RELEASE_TO_VIDEO_STATE) {
            smoothScroll(-getScrollY());
            currentState = VIDEO_STATE;
            stateText.setText("视频中");
        }
        if (getScrollY() == getTotalHeight() && currentState == VIDEO_STATE) {
            currentState = NORMAL_STATE;
        }
    }


    /**
     * smooth scroll to target val.
     *
     * @param dy
     */
    private void smoothScroll(int dy) {
        int duration = calculateDuration(Math.abs(dy));
        scroller.startScroll(0, getScrollY(), 0, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * calculate the duration for animation by dy.
     *
     * @param dy
     * @return
     */
    private int calculateDuration(int dy) {
        float fraction = dy * 1F / MAX_LOADING_LAYOUT_HEIGHT;
        return (int) (fraction * MAX_DURATION);
    }

    boolean isPullHeader;
    float dy;

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        this.dy = dy;
        scrollBy(0, dy);
        consumed[1] = dy;
    }

    private int getHeaderScrollRange() {
        return getTotalHeight() + OVERSCROLL_RANGE;
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("Carefree", "onNestedScroll: " + getScrollY());
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (dy < 0) return false;
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

//    @Override
//    public int getNestedScrollAxes() {
//        return ViewCompat.SCROLL_AXIS_VERTICAL;
//    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        super.scrollTo(x, y);
        if (y == 0) {
            currentState = VIDEO_STATE;
            stateText.setText("视频中");
        } else if (y == getTotalHeight() && currentState == REFRESHING_STATE) {
            currentState = NORMAL_STATE;
            stateText.setText("下拉刷新");
        } else if (currentState == NORMAL_STATE && y >= getRefreshHeight()) {
            if (y == getTotalHeight()) {
                stateText.setText("下拉刷新");
            }
            currentState = PULL_STATE;
        } else if (currentState == PULL_STATE && y < getRefreshHeight()) {
            currentState = RELEASE_TO_REFRESH_STATE;
            stateText.setText("松手刷新");
        } else if (currentState == RELEASE_TO_REFRESH_STATE && y <= VIDEO_ACTION_LINE && y > 0) {
            stateText.setText("松手看视频");
            currentState = RELEASE_TO_VIDEO_STATE;
        } else if (y > getRefreshHeight() && y < getTotalHeight() && currentState == RELEASE_TO_REFRESH_STATE) {
            stateText.setText("下拉刷新");
            currentState = RELEASE_TO_RESET_STATE;
        }
        if (y >= getTotalHeight() && currentState != NORMAL_STATE) {
            stateText.setText("下拉刷新");
            currentState = NORMAL_STATE;
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        isSmoothScrolling = isRelease && Math.abs(scroller.getCurrY()) > 8;
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    /**
     * complete the refresh state!
     */
    public void completeRefresh() {
        if (currentState == REFRESHING_STATE) {
            View view = this;
            stateText.setText("刷新完成");
            stateText.postDelayed(() -> {
                scroller.startScroll(0, getScrollY(), 0, getTotalHeight() - scroller.getCurrY(), 1200);
                ViewCompat.postInvalidateOnAnimation(view);
            }, 300);
        }
    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public int getRefreshHeight() {
        return video.getMeasuredHeight();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }


}
