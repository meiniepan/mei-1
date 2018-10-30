package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.ListRowResponse;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideBannerLoader;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/10/26.
 */

public class TimeBankMainFragment extends BaseFragment {
    @BindView(R.id.time_bank_main_list)
    RecyclerView timeBankMainList;
    @BindView(R.id.tb_main_banner)
    Banner tbMainBanner;
    private GridAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_time_bank_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initList();
        initBanner();
        initData();
    }

    private void initBanner() {
        tbMainBanner.setImageLoader(new GlideBannerLoader(true));
        tbMainBanner.setOffscreenPageLimit(4);
        tbMainBanner.setDelayTime(3000);
        tbMainBanner.isAutoPlay(true);
    }

    private void initList() {
        GridLayoutManager layout = new GridLayoutManager(mCtx, 2, LinearLayoutManager.VERTICAL, false);
        layout.setAutoMeasureEnabled(true);
        timeBankMainList.setLayoutManager(layout);
        timeBankMainList.setHasFixedSize(true);
        timeBankMainList.setNestedScrollingEnabled(false);
        timeBankMainList.addItemDecoration(CommonUtil.getRecyclerDivider(mCtx, 6));
        adapter = new GridAdapter();
        timeBankMainList.setAdapter(adapter);
    }

    ArrayList<String> bannerUrl = new ArrayList<>();

    private void initData() {
        ((BaseActivity) getActivity()).showLoadingView();
        EoscDataManager.getIns().getTable("samkunnbanb1 ", Constant.EOS_TIME_BANK, "task")
                .compose(RxUtil.switchSchedulers())
                .map(s -> {
                    ListRowResponse<VolunteerProjectBean> rowResponse = new Gson().fromJson(s, new TypeToken<ListRowResponse<VolunteerProjectBean>>() {
                    }.getType());
                    return rowResponse.rows;
                })
                .doOnNext(volunteerProjectBeans -> {
                    for (VolunteerProjectBean bean : volunteerProjectBeans) {
                        bannerUrl.add(bean.logofile);
                    }
                })
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<List<VolunteerProjectBean>>() {
                    @Override
                    public void onSuccess(List<VolunteerProjectBean> rowsBeans) {
                        ((BaseActivity) getActivity()).showContent();
                        adapter.setNewData(rowsBeans);
                        tbMainBanner.setImages(bannerUrl);
                        tbMainBanner.start();
                    }
                });

    }


    @OnClick({R.id.time_bank_main_project, R.id.time_bank_main_map, R.id.time_bank_main_more, R.id.tb_main_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_bank_main_project:
            case R.id.tb_main_search:
            case R.id.time_bank_main_more:
                startActivity(new Intent(mCtx, VolunteerProListActivity.class));
                break;
            case R.id.time_bank_main_map:

                break;
        }
    }


    class GridAdapter extends BaseQuickAdapter<VolunteerProjectBean, BaseHolder> {
        GridAdapter() {
            super(R.layout.item_tb_main_grid);
        }

        @Override
        protected void convert(BaseHolder baseHolder, VolunteerProjectBean volunteerProjectBean) {
            GlideUtils.loadImage(mContext, Constant.IPFS_URL + volunteerProjectBean.logofile, baseHolder.getView(R.id.item_tb_main_logo));
            baseHolder.setText(R.id.item_tb_main_title, volunteerProjectBean.name);
        }
    }
}
