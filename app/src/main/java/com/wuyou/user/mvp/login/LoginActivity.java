package com.wuyou.user.mvp.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.UnScrollViewPager;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tl_login_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_login_pager)
    UnScrollViewPager mViewPager;
    String[] mTitle = {"手机号快捷登录", "账户密码登录"};


    @Override
    protected void bindView(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //此方法用来显示tab上的名字
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position % mTitle.length];
            }

            @Override
            public Fragment getItem(int position) {
                //创建Fragment并返回
                Fragment fragment = null;
                if (position == 0) {
                    fragment = new PhoneLoginFragment();
                } else if (position == 1) {
                    fragment = new AccountLoginFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
        //将ViewPager关联到TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }


}
