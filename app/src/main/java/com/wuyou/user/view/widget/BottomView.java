package com.wuyou.user.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuyou.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2018/3/1.
 */

public class BottomView extends FrameLayout {
    @BindView(R.id.main_home)
    ImageView mainHome;
    @BindView(R.id.main_home_text)
    TextView mainHomeText;
    @BindView(R.id.main_home_layout)
    LinearLayout mainHomeLayout;
    @BindView(R.id.main_order)
    ImageView mainOrder;
    @BindView(R.id.main_order_text)
    TextView mainOrderText;
    @BindView(R.id.main_order_layout)
    LinearLayout mainOrderLayout;
    @BindView(R.id.main_help)
    ImageView mainHelp;
    @BindView(R.id.main_help_text)
    TextView mainHelpText;
    @BindView(R.id.main_help_layout)
    LinearLayout mainHelpLayout;
    @BindView(R.id.main_mine)
    ImageView mainMine;
    @BindView(R.id.main_mine_text)
    TextView mainMineText;
    @BindView(R.id.maim_mine_layout)
    LinearLayout maimMineLayout;

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_view, null);
        ButterKnife.bind(view);

    }



}
