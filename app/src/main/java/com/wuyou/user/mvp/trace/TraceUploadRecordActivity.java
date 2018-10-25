package com.wuyou.user.mvp.trace;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/22.
 */

public class TraceUploadRecordActivity extends BaseActivity {


    @BindView(R.id.trace_tab)
    TabLayout mTabLayout;
    @BindView(R.id.trace_pager)
    ViewPager mViewPager;
    String[] mTitle = {"待审核", "待确认", "已完成"};
    FragmentPagerAdapter fragmentPagerAdapter;
    private TraceUploadStatusFragment fragment1;
    private TraceUploadStatusFragment fragment2;
    private TraceUploadStatusFragment fragment3;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_trace_upload_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("溯源记录");
        initView();
    }

    private void initView() {
        //防止Activity被回收后Fragment状态不正确
        Bundle bundle1 = new Bundle();
        bundle1.putInt("h", 0);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("h", 1);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("h", 2);
        fragment1 = new TraceUploadStatusFragment();
        fragment1.setArguments(bundle1);
        fragment2 = new TraceUploadStatusFragment();
        fragment2.setArguments(bundle2);
        fragment3 = new TraceUploadStatusFragment();
        fragment3.setArguments(bundle3);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

                    fragment = fragment1;
                } else if (position == 1) {

                    fragment = fragment2;
                } else if (position == 2) {

                    fragment = fragment3;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitle.length;
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        //将ViewPager关联到TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
