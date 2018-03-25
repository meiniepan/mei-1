package com.wuyou.user.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.gnway.bangwo8sdk.Bangwo8SdkManager;
import com.gnway.bangwoba.global.Variable;
import com.gs.buluo.common.utils.DensityUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.help.HelpFragment;
import com.wuyou.user.mvp.home.HomeFragment;
import com.wuyou.user.mvp.mine.MineFragment;
import com.wuyou.user.mvp.order.OrderFragment;
import com.wuyou.user.service.HelpChatService;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.UnScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_tab)
    BottomNavigationViewEx bottomView;
    @BindView(R.id.main_pager)
    UnScrollViewPager viewPager;
    List<BaseFragment> fragments = new ArrayList<>();

    private long mKeyTime = 0;
    private OrderFragment orderFragment;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
//        QMUIStatusBarHelper.translucent(this,getResources().getColor(R.color.night_blue));
//        QMUIStatusBarHelper.setStatusBarLightMode(this);
        fragments.add(new HomeFragment());
        orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        fragments.add(new HelpFragment());
        fragments.add(new MineFragment());
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(3);
        bottomView.setupWithViewPager(viewPager, false);
        bottomView.enableAnimation(true);
        bottomView.enableShiftingMode(false);
        bottomView.enableItemShiftingMode(false);
        bottomView.setIconSize(1.0f * (DensityUtils.dip2px(getCtx(), 14)), 1.0f * (DensityUtils.dip2px(getCtx(), 14)));
        bottomView.setIconsMarginTop(DensityUtils.dip2px(getCtx(), 4));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (JZVideoPlayer.backPress()) {
                return false;
            }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setBarColor(R.color.transparent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
        super.onConfigurationChanged(newConfig);
    }
}
