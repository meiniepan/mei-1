package com.wuyou.user.mvp.block;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

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

    private BlockMainFragment blockMainFragment;
    private BlockTransactionFragment blockTransactionFragment;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.laidao_explore));
        blockTransactionFragment = new BlockTransactionFragment();
        blockMainFragment = new BlockMainFragment();
        blockPager.setAdapter(new BlockPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(blockPager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_block;
    }

    private class BlockPagerAdapter extends FragmentStatePagerAdapter {
        BlockPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return blockMainFragment;
            } else {
                return blockTransactionFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
