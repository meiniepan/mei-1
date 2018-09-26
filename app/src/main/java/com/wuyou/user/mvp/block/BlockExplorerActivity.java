package com.wuyou.user.mvp.block;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockExplorerActivity extends BaseActivity {
    @BindView(R.id.block_pager)
    ViewPager blockPager;
    @BindView(R.id.block_tab)
    TabLayout tabLayout;

    private String[] titles = {"主网", "实时"};

    @Override
    protected void bindView(Bundle savedInstanceState) {
        blockPager.setAdapter(new BlockPagerAdapter());
        tabLayout.setupWithViewPager(blockPager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_block;
    }

    private class BlockPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (position == 0) {
                return new BlockMainFragment();
            } else {
                return new BlockTranscationFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
