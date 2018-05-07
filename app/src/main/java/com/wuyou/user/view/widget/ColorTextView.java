package com.wuyou.user.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wuyou.user.R;

/**
 * Created by DELL on 2018/5/7.
 */

public class ColorTextView extends android.support.v7.widget.AppCompatTextView {
    public ColorTextView(Context context) {
        super(context);
    }

    public ColorTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            setTextColor(getResources().getColor(selectedColor));
        } else {
            setTextColor(getResources().getColor(R.color.common_dark));
        }
    }

    private int selectedColor = R.color.night_blue;

    public void setSelectedColor(int color) {
        selectedColor = color;
    }
}
