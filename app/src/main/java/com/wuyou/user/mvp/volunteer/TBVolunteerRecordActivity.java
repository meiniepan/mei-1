package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.mvp.trace.TraceAuthActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.CaptureActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DELL on 2018/10/26.
 */

public class TBVolunteerRecordActivity extends BaseActivity<TimeBankRecordContract.View, TimeBankRecordContract.Presenter> implements TimeBankRecordContract.View {

    @BindView(R.id.tb_record_tab)
    TabLayout tbRecordTab;
    @BindView(R.id.tb_record_pager)
    ViewPager tbRecordPager;

    private String[] titles = {"待参加", "已参加", "已关闭"};

    CarefreeRecyclerView attendingRecyclerView;
    CarefreeRecyclerView finishAttendRecyclerView;
    CarefreeRecyclerView closeRecyclerView;

    TBVolunteerAdapter attendingAdapter;
    TBVolunteerAdapter finishedAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_bank_volunteer_record;
    }

    @Override
    protected TimeBankRecordContract.Presenter getPresenter() {
        return new TimeBankPresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.project_record);
        tbRecordPager.setAdapter(new TBRecordPagerAdapter());
        tbRecordPager.setOffscreenPageLimit(3);
        tbRecordTab.setupWithViewPager(tbRecordPager);
        mPresenter.getRecordData();
    }

    private void navigateToDetail(VolunteerProjectBean bean) {
        Intent intent = new Intent(getCtx(), VolunteerProDetailActivity.class);
        intent.putExtra(Constant.VOLUNTEER_PROJECT, bean);
        startActivity(intent);
    }

    @Override
    public void registerSuccess(int position) {
        attendingRecyclerView.showProgressView();
        mPresenter.getRecordData();
    }

    @Override
    public void rewardSuccess(int position) {
        finishAttendRecyclerView.showProgressView();
        mPresenter.getRecordData();
    }

    @Override
    public void getAttendingDataSuccess(List<VolunteerProjectBean> data) {
        attendingRecyclerView.showContentView();
        attendingAdapter.setNewData(data);
        if (attendingAdapter.getData().size() == 0) {
            attendingRecyclerView.showEmptyView(getString(R.string.no_project_record));
        } else {
            attendingRecyclerView.showContentView();
        }
    }

    @Override
    public void getFinishAttendDataSuccess(List<VolunteerProjectBean> data) {
        finishAttendRecyclerView.showContentView();
        finishedAdapter.setNewData(data);
        if (finishedAdapter.getData().size() == 0) {
            finishAttendRecyclerView.showEmptyView(getString(R.string.no_project_record));
        } else {
            finishAttendRecyclerView.showContentView();
        }
    }

    @Override
    public void showError(String message, int res) {
        attendingRecyclerView.showErrorView(message);
        finishAttendRecyclerView.showErrorView(message);
    }

    class TBRecordPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (position == 2) {
                closeRecyclerView = new CarefreeRecyclerView(getCtx());
                closeRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                closeRecyclerView.showEmptyView("暂无关闭项目");
                container.addView(closeRecyclerView);
                return closeRecyclerView;
            } else if (position == 1) {
                finishAttendRecyclerView = new CarefreeRecyclerView(getCtx());
                finishAttendRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                finishAttendRecyclerView.setRefreshAction(() -> mPresenter.getRecordData());
                finishedAdapter = new TBVolunteerAdapter(position);
                finishAttendRecyclerView.setAdapter(finishedAdapter);
                finishedAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> navigateToDetail((VolunteerProjectBean) baseQuickAdapter.getData().get(i)));
                finishAttendRecyclerView.showProgressView();
                container.addView(finishAttendRecyclerView);
                return finishAttendRecyclerView;
            } else {
                attendingRecyclerView = new CarefreeRecyclerView(getCtx());
                attendingRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                attendingRecyclerView.setRefreshAction(() -> mPresenter.getRecordData());
                attendingAdapter = new TBVolunteerAdapter(position);
                attendingRecyclerView.setAdapter(attendingAdapter);
                attendingAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> navigateToDetail((VolunteerProjectBean) baseQuickAdapter.getData().get(i)));
                attendingRecyclerView.showProgressView();
                container.addView(attendingRecyclerView);
                return attendingRecyclerView;
            }
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

    class TBVolunteerAdapter extends BaseQuickAdapter<VolunteerProjectBean, BaseHolder> {
        private int status;

        public TBVolunteerAdapter(int status) {
            super(R.layout.item_tb_volunteer_record);
            this.status = status;
        }

        @Override
        protected void convert(BaseHolder baseHolder, VolunteerProjectBean rowsBean) {
            baseHolder.setText(R.id.item_tb_record_name, rowsBean.name);
            GlideUtils.loadImage(getCtx(), Constant.IPFS_URL + rowsBean.logofile, baseHolder.getView(R.id.item_tb_record_logo));
            TextView actionView = baseHolder.getView(R.id.item_tb_record_action);
            TextView rewardsView = baseHolder.getView(R.id.item_tb_record_rewards);
            if (status == 0) {
                actionView.setText("去签到");
            } else if (status == 1) {
                rewardsView.setVisibility(View.VISIBLE);
                actionView.setText("溯源认证");
                if (rowsBean.rewardsStatus == 2) {
                    rewardsView.setText(R.string.tb_wait_rewards);
                    rewardsView.setEnabled(true);
                } else {
                    rewardsView.setText(R.string.tb_already_rewards);
                    rewardsView.setEnabled(false);
                }
            } else {
                rewardsView.setVisibility(View.GONE);
                actionView.setVisibility(View.GONE);
            }

            actionView.setOnClickListener(v -> {
                if (status == 0) {
                    navigateToScan();
                } else if (status == 1) {
                    navigateToTrace(rowsBean.name);
                }
            });

            rewardsView.setOnClickListener(v -> {
                showLoadingDialog();
                mPresenter.rewardProject(baseHolder.getAdapterPosition(), rowsBean);
            });
        }

        private void navigateToTrace(String name) {
            Intent intent = new Intent(getCtx(), TraceAuthActivity.class);
            intent.putExtra(Constant.TRACE_KEY_WORD, name);
            startActivity(intent);

        }
    }

    private void navigateToScan() {
        Intent intent = new Intent(getCtx(), CaptureActivity.class);
        startActivityForResult(intent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            registerSuccess(0);
        }
    }
}
