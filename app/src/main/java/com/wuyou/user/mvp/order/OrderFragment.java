package com.wuyou.user.mvp.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.wuyou.user.R;
import com.wuyou.user.data.local.TabEntity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.panel.EnvironmentChoosePanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.order_tab)
    public TabLayout orderTab;
    @BindView(R.id.order_pager)
    ViewPager orderPager;
    MyFragmentAdapter fragmentPagerAdapter;
    private OrderStatusFragment fragment1;
    private OrderStatusFragment fragment2;
    private OrderStatusFragment fragment3;
    private OrderStatusFragment fragment4;
    private OrderStatusFragment fragment5;

    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] titles;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.serving), getString(R.string.wait_comment), getString(R.string.complete)};
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        //防止Activity被回收后Fragment状态不正确
        Bundle bundle1 = new Bundle();
        bundle1.putInt("h", 0);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("h", 1);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("h", 2);
        Bundle bundle4 = new Bundle();
        bundle4.putInt("h", 3);
        Bundle bundle5 = new Bundle();
        bundle5.putInt("h", 4);
        fragment1 = new OrderStatusFragment();
        fragment1.setArguments(bundle1);
        fragment2 = new OrderStatusFragment();
        fragment2.setArguments(bundle2);
        fragment3 = new OrderStatusFragment();
        fragment3.setArguments(bundle3);
        fragment4 = new OrderStatusFragment();
        fragment4.setArguments(bundle4);
        fragment5 = new OrderStatusFragment();
        fragment5.setArguments(bundle5);
        fragmentPagerAdapter = new MyFragmentAdapter(getChildFragmentManager());
        orderPager.setAdapter(fragmentPagerAdapter);
        orderTab.setupWithViewPager(orderPager);
        for (int i = 0; i < orderTab.getTabCount(); i++) {
            TabLayout.Tab tab = orderTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(fragmentPagerAdapter.getTabView(i));
            }
        }
        orderTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.textView))
                        .setTextColor(getResources().getColor(R.color.night_blue));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.textView))
                        .setTextColor(getResources().getColor(R.color.common_dark));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getActivity().findViewById(R.id.back_door).setOnClickListener(v -> showChangeEnvironment());
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

    private int clickTime = 0;
    private long firstTime = 0;

    private void showChangeEnvironment() {
        if (clickTime == 0) {
            firstTime = System.currentTimeMillis();
        }
        clickTime++;
        if (clickTime == 5) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - firstTime <= 2000) {
                EnvironmentChoosePanel choosePanel = new EnvironmentChoosePanel(getContext());
                choosePanel.show();
                clickTime = 0;
                firstTime = 0;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clickTime = 0;
        firstTime = 0;
    }
     class MyFragmentAdapter extends FragmentPagerAdapter{
         public MyFragmentAdapter(FragmentManager fm) {
             super(fm);
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
             } else if (position == 3) {
                 fragment = fragment4;
             } else if (position == 4) {
                 fragment = fragment5;
             }
             return fragment;
         }

         @Override
         public int getCount() {
             return titles.length;
         }

         public View getTabView(int position) {
             View v = LayoutInflater.from(getActivity()).inflate(R.layout.order_tab, null);
             TextView tv = (TextView) v.findViewById(R.id.textView);
             tv.setText(titles[position]);
             if (position == 0){
                 tv.setTextColor(getResources().getColor(R.color.night_blue));
             }
             return v;
         }
     }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void getEventBus(Integer num) {
        if (num != null) {
            //接收到红点事件
            TabLayout.Tab tab = orderTab.getTabAt(num);
            TextView hasNewMsg = (TextView)tab.getCustomView().findViewById(R.id.has_new_msg);
//            hasNewMsg.setVisibility(View.VISIBLE);
        }
    }
}
