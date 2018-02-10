package com.wuyou.user.mvp.store;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.bean.StoreBean;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.StoreApis;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/10.
 */

public class StoreDetailActivity extends BaseActivity {
    @BindView(R.id.store_title)
    TextView storeTitle;
    @BindView(R.id.store_accepted_number)
    TextView storeAcceptedNumber;
    @BindView(R.id.store_finish_percent)
    TextView storeFinishPercent;
    @BindView(R.id.store_good_percent)
    TextView storeGoodPercent;
    @BindView(R.id.store_serve_time)
    TextView storeServeTime;
    @BindView(R.id.store_tab)
    TabLayout storeTab;
    @BindView(R.id.store_pager)
    ViewPager pager;
    private StoreServeFragment fragment;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String storeId = getIntent().getStringExtra(Constant.STORE_ID);
        fragment = new StoreServeFragment();
        fragment.setTitle("全部项目");
        StoreCommentFragment fragment1 = new StoreCommentFragment();
        fragment1.setTitle("用户评论");
        ArrayList<BaseFragment> list = new ArrayList<>();
        list.add(fragment);
        list.add(fragment1);
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));
        storeTab.setupWithViewPager(pager);
        getData(storeId);
    }

    private void getData(String storeId) {
        CarefreeRetrofit.getInstance().createApi(StoreApis.class)
                .getStoreDetail(storeId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<StoreBean> storeBeanBaseResponse) {
                        setData(storeBeanBaseResponse.data);
                    }
                });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_detail;
    }

    @OnClick(R.id.store_menu)
    public void onViewClicked() {
    }

    public void setData(StoreBean data) {
        fragment.setData(data.list);
        storeTitle.setText(data.name);
        storeAcceptedNumber.setText(data.received);
        storeGoodPercent.setText(data.high_praise_proportion);
        storeServeTime.setText(data.service_start_at + ":" + data.service_end_at);
//        storeFinishPercent

    }
}
