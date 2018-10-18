package com.wuyou.user.mvp.vote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wuyou.user.R;
import com.wuyou.user.data.api.EosVoteListBean;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.MainActivity;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by DELL on 2018/10/15.
 */

public class VoteActivity extends BaseActivity {
    @BindView(R.id.vote_pager)
    ViewPager votePager;
    @BindView(R.id.vote_tab)
    TabLayout voteTab;
    @BindView(R.id.bottom_alpha)
    View bottomAlpha;

    private String[] titles = {"投票列表", "我的投票"};
    private VoteListFragment voteListFragment;
    private MyVoteListFragment myVoteListFragment;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.vote);
        setTitleIcon(R.mipmap.vote_warn, v -> showWarnActivity());

        voteListFragment = new VoteListFragment();
        voteListFragment.setOwnerActivity(this);
        myVoteListFragment = new MyVoteListFragment();
        votePager.setAdapter(new VotePagerAdapter(getSupportFragmentManager()));
        voteTab.setupWithViewPager(votePager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_vote;
    }

    private void showWarnActivity() {
        startActivity(new Intent(getCtx(), VoteExplainActivity.class));

    }

    public void setBottomAlpha(boolean alpha) {
        if (alpha){
            bottomAlpha.setVisibility(View.VISIBLE);
        }else {
            bottomAlpha.setVisibility(View.GONE);
        }
    }

    public void setMyVotedMap(HashMap<String,EosVoteListBean.RowsBean> myVotedMap) {
        myVoteListFragment.setVotedData(myVotedMap);
    }

    private class VotePagerAdapter extends FragmentPagerAdapter {

        public VotePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return voteListFragment;
            } else {
                return myVoteListFragment;
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
