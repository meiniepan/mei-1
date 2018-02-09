package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.gs.buluo.common.utils.DensityUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.help.HelpFragment;
import com.wuyou.user.mvp.home.HomeFragment;
import com.wuyou.user.mvp.mine.MineFragment;
import com.wuyou.user.mvp.order.OrderFragment;
import com.wuyou.user.util.QMUIStatusBarHelper;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_tab)
    BottomNavigationViewEx bottomView;
    @BindView(R.id.main_pager)
    ViewPager viewPager;

    List<BaseFragment> fragments = new ArrayList<>();

    private long mKeyTime = 0;
    private OrderFragment orderFragment;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int intExtra = intent.getIntExtra(Constant.MAIN_FLAG, 0);
        switch (intExtra){
            case 1: //登录跳转
                EventBus.getDefault().post(new LoginEvent());
                break;
        }
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
//        QMUIStatusBarHelper.translucent(this,getResources().getColor(R.color.night_blue));
//        QMUIStatusBarHelper.setStatusBarLightMode(this);
        fragments.add(new HomeFragment());
        fragments.add(new HelpFragment());
        orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        fragments.add(new MineFragment());
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(3);
        bottomView.setupWithViewPager(viewPager, false);
        bottomView.enableAnimation(true);
        bottomView.enableShiftingMode(false);
        bottomView.enableItemShiftingMode(false);
        bottomView.setIconSize(1.0f * (DensityUtils.dip2px(getCtx(), 12)), 1.0f * (DensityUtils.dip2px(getCtx(), 12)));
        bottomView.setIconsMarginTop(DensityUtils.dip2px(getCtx(), -3));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mKeyTime) > 2000) {
                mKeyTime = System.currentTimeMillis();
                Toast.makeText(this, R.string.exit_app, Toast.LENGTH_LONG).show();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
