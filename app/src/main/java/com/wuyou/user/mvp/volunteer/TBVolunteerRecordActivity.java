package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.view.activity.BaseActivity;
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
    CarefreeRecyclerView CloseRecyclerView;

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

    @Override
    public void registerSuccess(int position) {


    }

    @Override
    public void rewardSuccess(int position) {

    }

    @Override
    public void getAttendingDataSuccess(List<VolunteerProjectBean> data) {
        attendingAdapter.setNewData(data);
        if (attendingAdapter.getData().size() == 0) {
            attendingRecyclerView.showEmptyView(getString(R.string.no_project_record));
        } else {
            attendingRecyclerView.showContentView();
        }
    }

    @Override
    public void getFinishAttendDataSuccess(List<VolunteerProjectBean> data) {
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
                CloseRecyclerView = new CarefreeRecyclerView(getCtx());
                CloseRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                CloseRecyclerView.showEmptyView("暂无关闭项目");
                container.addView(CloseRecyclerView);
                return CloseRecyclerView;
            } else if (position == 1) {
                finishAttendRecyclerView = new CarefreeRecyclerView(getCtx());
                finishAttendRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                finishedAdapter = new TBVolunteerAdapter(position);
                finishAttendRecyclerView.setAdapter(finishedAdapter);
                finishAttendRecyclerView.showProgressView();
                container.addView(finishAttendRecyclerView);
                return finishAttendRecyclerView;
            } else {
                attendingRecyclerView = new CarefreeRecyclerView(getCtx());
                attendingRecyclerView.setEmptyIcon(R.mipmap.empty_score);
                attendingAdapter = new TBVolunteerAdapter(position);
                attendingRecyclerView.setAdapter(attendingAdapter);
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
        }
    }
}
