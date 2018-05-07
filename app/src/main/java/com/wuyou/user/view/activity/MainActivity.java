package com.wuyou.user.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.help.HelpFragment;
import com.wuyou.user.mvp.home.HomeFragment;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.mvp.mine.MineFragment;
import com.wuyou.user.mvp.order.OrderFragment;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.UnScrollViewPager;
import com.yinglan.alphatabs.AlphaTabView;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_tab)
    AlphaTabsIndicator bottomView;
    @BindView(R.id.main_pager)
    UnScrollViewPager viewPager;
    List<BaseFragment> fragments = new ArrayList<>();

    private long mKeyTime = 0;
    private OrderFragment orderFragment;

    @Override
    protected void onNewIntent(Intent intent) {
        int flag = intent.getIntExtra(Constant.MAIN_FLAG,0);
        if (flag==1){
            viewPager.setCurrentItem(1);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
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
        bottomView.setViewPager(viewPager);
        AlphaTabView tabView = bottomView.getTabView(3);
        tabView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && CarefreeDaoSession.getInstance().getUserInfo() == null) {
                    Intent intent = new Intent(MainActivity.this.getCtx(), LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        bottomView.getTabView(1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    orderFragment.setStatus(0);
                }
                return false;
            }
        });

        ShareConfig config = ShareConfig.instance().wxId(Constant.WX_ID).wxSecret(Constant.WX_SECRET);
        ShareManager.init(config);
        CrashReport.putUserData(getApplicationContext(), "userkey", CarefreeDaoSession.getInstance().getUserInfo() == null ? "unLogin" : CarefreeDaoSession.getInstance().getUserInfo().getMobile());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        viewPager.setCurrentItem(0);
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
