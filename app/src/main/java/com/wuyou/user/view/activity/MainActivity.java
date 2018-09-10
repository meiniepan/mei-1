package com.wuyou.user.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.gs.buluo.common.network.TokenEvent;
import com.tencent.bugly.crashreport.CrashReport;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.home.HomeFragment;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.mvp.mine.MineFragment;
import com.wuyou.user.mvp.order.OrderFragment;
import com.wuyou.user.util.QMUIStatusBarHelper;
import com.wuyou.user.view.fragment.ActivityFragment;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.UnScrollViewPager;
import com.wuyou.user.view.widget.bottom.AlphaTabView;
import com.wuyou.user.view.widget.bottom.AlphaTabsIndicator;

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

    @Override
    protected void onNewIntent(Intent intent) {
        int flag = intent.getIntExtra(Constant.MAIN_FLAG, 0);
        if (flag == 1) {
            viewPager.setCurrentItem(1);
        }
        String activityUrl = intent.getStringExtra(Constant.ACTIVITY_URL);
        goActivity(activityUrl);
        super.onNewIntent(intent);
    }

    private void goActivity(String activityUrl) {
        if (TextUtils.isEmpty(activityUrl)) {
            return;
        }
        Intent intent = new Intent(getCtx(), WebActivity.class);
        Uri uri = Uri.parse(activityUrl);
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
        if (userInfo == null) {
            if (TextUtils.isEmpty(uri.getQuery())) {
                intent.putExtra(Constant.WEB_INTENT, activityUrl);
            } else {
                intent.putExtra(Constant.WEB_INTENT, activityUrl);
            }
        } else {
            if (TextUtils.isEmpty(uri.getQuery())) {
                intent.putExtra(Constant.WEB_INTENT, activityUrl + "?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + userInfo.getToken());
            } else {
                intent.putExtra(Constant.WEB_INTENT, activityUrl + "&user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + userInfo.getToken());
            }
        }
        startActivity(intent);
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
        disableFitSystemWindow();
        setBarColor(R.color.transparent);
        fragments.add(new HomeFragment());
        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        fragments.add(new ActivityFragment());
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
    }

    @Override
    protected void init() {
        CarefreeApplication.getInstance().ManualCheckOnForceUpdate();
        goActivity(getIntent().getStringExtra(Constant.ACTIVITY_URL));
        NotificationManagerCompat.from(this).areNotificationsEnabled();
        ShareConfig config = ShareConfig.instance().wxId(Constant.WX_ID).wxSecret(Constant.WX_SECRET);
        ShareManager.init(config);
        CrashReport.putUserData(getApplicationContext(), "userkey", CarefreeDaoSession.getInstance().getUserInfo() == null ? "unLogin" : CarefreeDaoSession.getInstance().getUserInfo().getMobile());
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenExpired(TokenEvent event) {
        CarefreeDaoSession.getInstance().clearUserInfo();
        EventBus.getDefault().post(new LoginEvent());
        Intent intent = new Intent(getCtx(), LoginActivity.class);
        startActivity(intent);
    }
}
