package com.wuyou.user.mvp.order;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wuyou.user.R;
import com.wuyou.user.adapter.OrderFragmentAdapter;
import com.wuyou.user.bean.TabEntity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.order_tab)
    CommonTabLayout orderTab;
    @BindView(R.id.order_pager)
    ViewPager orderPager;
    Unbinder unbinder;

    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String[] titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.wait_comment), getString(R.string.complete)};
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        OrderFragmentAdapter adapter =
                new OrderFragmentAdapter(getActivity().getSupportFragmentManager(), Arrays.asList(titles));
        orderPager.setAdapter(adapter);
        orderTab.setTabData(mTabEntities);
        orderTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                orderPager.setCurrentItem(position);
//                if (position == 2)
//                    readMessage();
            }

            @Override
            public void onTabReselect(int position) {
//                if (position == 0) {
//                    tabLayout.hideMsg(0);
//                }
            }
        });
    }

    @Override
    public void showError(int res) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
