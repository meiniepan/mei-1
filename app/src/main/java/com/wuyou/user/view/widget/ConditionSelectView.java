package com.wuyou.user.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuyou.user.R;

import org.w3c.dom.Text;

/**
 * Created by DELL on 2018/10/15.
 */

public class ConditionSelectView extends LinearLayout{

    private TextView textView;
    private ImageView imageView;
    private String text;
    public ConditionSelectView(Context context) {
        this(context,null);
    }

    public ConditionSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConditionSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ConditionSelectView, 0, 0);
        try {
            text = a.getText(R.styleable.ConditionSelectView_text).toString();
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        textView = new TextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.common_gray));
        textView.setText(text);
        imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.pull_gray);
        addView(textView);
        addView(imageView);
    }

    public void setTextView(String content){
        textView.setText(content);
    }

    public void setSelected(boolean selected){
        if (selected){
            textView.setTextColor(getResources().getColor(R.color.night_blue));
            imageView.setImageResource(R.mipmap.pull_blue);
        }else {
            textView.setTextColor(getResources().getColor(R.color.common_gray));
            imageView.setImageResource(R.mipmap.pull_gray);
        }
    }
}
