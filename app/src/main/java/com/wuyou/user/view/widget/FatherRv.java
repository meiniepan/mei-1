package com.wuyou.user.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Solang on 2018/10/16.
 */

public class FatherRv extends RecyclerView {
    public FatherRv(Context context) {
        super(context);
    }

    public FatherRv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FatherRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }
}
