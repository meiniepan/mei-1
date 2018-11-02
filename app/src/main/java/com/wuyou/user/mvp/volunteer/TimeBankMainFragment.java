package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.ListRowResponse;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.data.remote.ServeSites;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideBannerLoader;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.HomeMapActivity;
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
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            VolunteerProjectBean bean = adapter.getData().get(i);
            Intent intent = new Intent(mCtx, VolunteerProDetailActivity.class);
            intent.putExtra(Constant.VOLUNTEER_PROJECT, bean);
            startActivity(intent);
        });
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
    ArrayList<ServeSites> sites = new ArrayList<>();

    private void initData() {
        ((BaseActivity) getActivity()).showLoadingView();
        EoscDataManager.getIns().getTable(Constant.TB_OWNER_ACCOUNT, Constant.EOS_TIME_BANK, "task", "", "", "", 6)
                .compose(RxUtil.switchSchedulers())
                .map(s -> {
                    ListRowResponse<VolunteerProjectBean> rowResponse = new Gson().fromJson(s, new TypeToken<ListRowResponse<VolunteerProjectBean>>() {
                    }.getType());
                    return rowResponse.rows;
                })
                .doOnNext(volunteerProjectBeans -> {
                    ServeSites serveSites;
                    for (VolunteerProjectBean bean : volunteerProjectBeans) {
                        serveSites = new ServeSites(bean.name, bean.address, bean.latitude*1.0d/1000000, bean.longitude*1.0d/1000000);
                        sites.add(serveSites);
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

                    @Override
                    protected void onFail(ApiException e) {
                        ((BaseActivity) getActivity()).showError(e.getDisplayMessage(), e.getCode());
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        ((BaseActivity) getActivity()).showError(message.message, code);
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
                Intent intent = new Intent(mCtx, HomeMapActivity.class);
                intent.putParcelableArrayListExtra(Constant.SITE_LIST, sites);
                startActivity(intent);
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
