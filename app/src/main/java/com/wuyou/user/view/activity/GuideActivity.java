package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.wuyou.user.R;
import com.wuyou.user.adapter.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2016/12/27.
 */
public class GuideActivity extends BaseActivity {
    @BindView(R.id.guide_pager)
    ViewPager viewPager;
    @BindView(R.id.guide_button)
    Button guideButton;

    GuidePagerAdapter vAdapter;
    private List<Integer> list;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        list = new ArrayList<>();
        list.add(R.mipmap.guide_1);
        list.add(R.mipmap.guide_2);
        list.add(R.mipmap.guide_3);
        vAdapter = new GuidePagerAdapter(this, list);
        viewPager.setAdapter(vAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    guideButton.setClickable(true);
                } else {
                    guideButton.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_guide;
    }

    public void startAct(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
