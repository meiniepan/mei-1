package com.wuyou.user.view.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wbn on 2018/9/3.
 */

public class GridLineSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount; //行数
    private int spacing; //间隔

    public GridLineSpacingItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //这里是关键，需要根据你有几列来判断
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (column < spanCount-1) {
            outRect.top = 0;
        }
        outRect.bottom = spacing;
    }
}