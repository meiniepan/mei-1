package com.wuyou.user.mvp.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.wuyou.user.R;
import com.wuyou.user.adapter.OrderFragmentAdapter;
import com.wuyou.user.bean.TabEntity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.order_tab)
    TabLayout orderTab;
    @BindView(R.id.order_pager)
    ViewPager orderPager;

    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String[] titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.serving), getString(R.string.wait_comment), getString(R.string.complete)};
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        OrderFragmentAdapter adapter = new OrderFragmentAdapter(getActivity().getSupportFragmentManager(), Arrays.asList(titles));
        orderPager.setAdapter(adapter);
        orderTab.setupWithViewPager(orderPager);
    }

    @Override
    public void fetchData() {
        orderPager.setCurrentItem(0);
    }

    public void setStatus(int item) {
        if (orderPager == null) return;
        if (orderPager.getCurrentItem() != 1)
            orderPager.setCurrentItem(item);
    }
}
