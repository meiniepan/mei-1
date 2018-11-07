package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VolunteerPositionListAdapter;
import com.wuyou.user.data.api.DetailFileBean;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.data.remote.ServeSites;
import com.wuyou.user.mvp.trace.TraceAuthActivity;
import com.wuyou.user.network.IPFSRetrofit;
import com.wuyou.user.network.apis.NodeosApi;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.CaptureActivity;
import com.wuyou.user.view.activity.HomeMapActivity;
import com.wuyou.user.view.widget.panel.PositionChoosePanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/29.
 */

public class VolunteerProDetailActivity extends BaseActivity<TimeBankRecordContract.View, TimeBankRecordContract.Presenter> implements TimeBankRecordContract.View {
    VolunteerProjectBean data;
    @BindView(R.id.iv_volunteer_detail_logo)
    ImageView ivVolunteerDetailLogo;
    @BindView(R.id.tv_volunteer_detail_pro_name)
    TextView tvVolunteerDetailProName;
    @BindView(R.id.tv_volunteer_detail_pro_obj)
    TextView tvVolunteerDetailProObj;
    @BindView(R.id.tv_volunteer_detail_pro_serve_time)
    TextView tvVolunteerDetailProServeTime;
    @BindView(R.id.tv_volunteer_detail_pro_serve_address)
    TextView tvVolunteerDetailProServeAddress;
    @BindView(R.id.tv_volunteer_detail_pro_group)
    TextView tvVolunteerDetailProGroup;
    @BindView(R.id.tv_volunteer_detail_pro_contact)
    TextView tvVolunteerDetailProContact;
    @BindView(R.id.tv_volunteer_detail_pro_phone)
    TextView tvVolunteerDetailProPhone;
    @BindView(R.id.tv_volunteer_detail_pro_email)
    TextView tvVolunteerDetailProEmail;
    @BindView(R.id.tv_volunteer_detail_pro_recruit_time)
    TextView tvVolunteerDetailProRecruitTime;
    @BindView(R.id.rv_volunteer_detail_position)
    RecyclerView recyclerView;
    @BindView(R.id.tv_volunteer_detail_pro_intro)
    TextView tvVolunteerDetailProIntro;
    @BindView(R.id.tv_volunteer_pro_detail_rewards)
    TextView tvRewards;
    @BindView(R.id.btn_volunteer_detail_pro_left)
    TextView volunteerDetailLeft;
    @BindView(R.id.btn_volunteer_detail_pro_right)
    TextView volunteerDetailRight;
    VolunteerPositionListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_volunteer_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.project_detail));
        data = getIntent().getParcelableExtra(Constant.VOLUNTEER_PROJECT);
        initUI();
        getDetailFile();
        setUpStatus();
    }

    private void setUpStatus() {
        long UTCTime = System.currentTimeMillis();
        Log.e("Carefree", "setUpStatus: " + UTCTime);
        if (UTCTime < data.enroll_time * 1000) {
            volunteerDetailRight.setText("未开始");
            volunteerDetailRight.setEnabled(false);
        } else if (UTCTime > data.enroll_end_time * 1000) {
            volunteerDetailRight.setText("已结束");
            volunteerDetailRight.setEnabled(false);
        }
        if (data.rewardsStatus == 1) {
            volunteerDetailLeft.setVisibility(View.GONE);
            volunteerDetailRight.setText(R.string.to_sign_up);
        } else if (data.rewardsStatus == 2) {
            volunteerDetailLeft.setText(R.string.trace_auth);
            volunteerDetailRight.setText(R.string.tb_wait_rewards);
            volunteerDetailRight.setEnabled(true);
        } else if (data.rewardsStatus == 3) {
            volunteerDetailLeft.setText(R.string.trace_auth);
            volunteerDetailRight.setText(R.string.tb_already_rewards);
            volunteerDetailRight.setEnabled(false);
        }
    }

    private void getDetailFile() {
        if (TextUtils.isEmpty(data.detailfile)) return;
        IPFSRetrofit.getInstance().createApi(NodeosApi.class).getIPFSData(data.detailfile)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        DetailFileBean detailFileBean = new Gson().fromJson(jsonObject, DetailFileBean.class);
                        initDetailUI(detailFileBean);
                    }
                });

    }

    @Override
    protected TimeBankRecordContract.Presenter getPresenter() {
        return new TimeBankPresenter();
    }

    private void initDetailUI(DetailFileBean detailFileBean) {
        tvVolunteerDetailProServeAddress.setText(detailFileBean.address);
        tvVolunteerDetailProGroup.setText(detailFileBean.group);
        tvVolunteerDetailProContact.setText(detailFileBean.contact);
        tvVolunteerDetailProPhone.setText(detailFileBean.mobile);
        tvVolunteerDetailProIntro.setText(detailFileBean.description);
        tvVolunteerDetailProEmail.setText(detailFileBean.email);
    }

    private void initUI() {
        GlideUtils.loadRoundCornerImage(getCtx(), Constant.IPFS_URL + data.logofile, ivVolunteerDetailLogo);
        tvVolunteerDetailProName.setText(data.name);
        tvVolunteerDetailProServeTime.setText(TribeDateUtils.dateFormat(new Date(data.service_time * 1000)) + " 至 " + TribeDateUtils.dateFormat(new Date(data.service_end_time * 1000)));
        tvVolunteerDetailProServeAddress.setText(data.address);
        tvVolunteerDetailProRecruitTime.setText(TribeDateUtils.dateFormat(new Date(data.enroll_time * 1000)) + " 至 " + TribeDateUtils.dateFormat(new Date(data.enroll_end_time * 1000)));
        tvRewards.setText("服务可获得" + getRewards());
        initRv();
    }

    private String getRewards() {
        float rewardsMin = 0, rewardsMax = 0;
        boolean isFirst = true;
        for (VolunteerProjectBean.PositionsBean e : data.positions
                ) {
            float rew = Float.parseFloat(e.rewards.replaceAll("EOS", "").replaceAll(" ", ""));
            if (isFirst) {
                isFirst = false;
                rewardsMin = rewardsMax = rew;
            } else {
                if (rew < rewardsMin) {
                    rewardsMin = rew;
                } else if (rew > rewardsMax) {
                    rewardsMax = rew;
                }
            }
        }
        String rewards;
        if (rewardsMin == rewardsMax) {
            rewards = CommonUtil.formaEos(rewardsMin);
        } else {
            rewards = CommonUtil.formaEos(rewardsMin) + " - " + CommonUtil.formaEos(rewardsMax);
        }
        return rewards;
    }

    private void initRv() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getCtx());
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new VolunteerPositionListAdapter(R.layout.item_volunteer_project_position);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data.positions);
    }


    @OnClick({R.id.btn_volunteer_detail_pro_left, R.id.btn_volunteer_detail_pro_right, R.id.volunteer_detail_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_volunteer_detail_pro_left:
                if (data.rewardsStatus == 0) {
                    ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                } else {
                    Intent intent = new Intent(getCtx(), TraceAuthActivity.class);
                    intent.putExtra(Constant.TRACE_KEY_WORD, data.name);
                    startActivity(intent);
                }
                break;
            case R.id.btn_volunteer_detail_pro_right:
                if (data.rewardsStatus == 1) {
                    navigateToCapture();
                } else if (data.rewardsStatus == 2) {
                    showLoadingDialog();
                    mPresenter.rewardProject(0, data);
                } else if (data.rewardsStatus == 0) {
                    PositionChoosePanel choosePanel = new PositionChoosePanel(getCtx(), data);
                    choosePanel.show();
                }
                break;
            case R.id.volunteer_detail_location:
                Intent intent = new Intent(getCtx(), HomeMapActivity.class);
                ArrayList<ServeSites> list = new ArrayList<>();
                list.add(new ServeSites(data.name, data.address, data.latitude * 1.0f / 1000000, data.longitude * 1.0f / 1000000));
                intent.putExtra(Constant.SITE_LIST, list);
                startActivity(intent);
                break;
        }
    }

    private void navigateToCapture() {
        Intent intent = new Intent(getCtx(), CaptureActivity.class);
        startActivityForResult(intent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            this.data.rewardsStatus = 2;
            setUpStatus();
            setResult(RESULT_OK);//扫码签到成功
        }
    }


    @Override
    public void rewardSuccess(int position) {
        setResult(203);
    }

    @Override
    public void getAttendingDataSuccess(List<VolunteerProjectBean> data) {
    }

    @Override
    public void getFinishAttendDataSuccess(List<VolunteerProjectBean> data) {
    }
}
