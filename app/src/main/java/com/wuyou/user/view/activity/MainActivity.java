package com.wuyou.user.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.mvp.help.HelpFragment;
import com.wuyou.user.mvp.home.HomeFragment;
import com.wuyou.user.mvp.mine.MineFragment;
import com.wuyou.user.mvp.order.OrderFragment;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_tab)
    BottomNavigationViewEx bottomView;
    @BindView(R.id.main_pager)
    ViewPager viewPager;

    List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        fragments.add(new HomeFragment());
        fragments.add(new HelpFragment());
        fragments.add(new OrderFragment());
        fragments.add(new MineFragment());
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        bottomView.setupWithViewPager(viewPager, false);
        bottomView.setIconVisibility(false);
        bottomView.enableShiftingMode(false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }
}
