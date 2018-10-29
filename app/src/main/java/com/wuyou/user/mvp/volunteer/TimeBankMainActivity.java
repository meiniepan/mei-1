package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;

import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.UnScrollViewPager;
import com.wuyou.user.view.widget.bottom.AlphaTabsIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by DELL on 2018/10/26.
 */

public class TimeBankMainActivity extends BaseActivity {
    @BindView(R.id.time_main_pager)
    UnScrollViewPager timeMainPager;
    @BindView(R.id.time_main_tab)
    AlphaTabsIndicator timeMainTab;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_bank_main;
    }


    List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.time_bank);
        fragments.add(new TimeBankMainFragment());
        fragments.add(new TimeBankMineFragment());
        timeMainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        timeMainTab.setViewPager(timeMainPager);
    }
}
