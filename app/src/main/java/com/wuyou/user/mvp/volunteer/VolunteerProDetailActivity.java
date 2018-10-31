package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.wuyou.user.network.IPFSRetrofit;
import com.wuyou.user.network.apis.NodeosApi;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.panel.PositionChoosePanel;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/29.
 */

public class VolunteerProDetailActivity extends BaseActivity {
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
    }

    private void getDetailFile() {
        IPFSRetrofit.getInstance().createApi(NodeosApi.class).getIPFSData("QmaKmgyobAUdT5Hxciymmd1QKur3b7xS6RrjeHPXMdw9ei")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        DetailFileBean detailFileBean = new Gson().fromJson(jsonObject,DetailFileBean.class);
                        initDetailUI(detailFileBean);
                    }
                });

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
        tvVolunteerDetailProServeTime.setText(TribeDateUtils.dateFormat5(new Date(data.service_time * 1000)) + " 至 " + TribeDateUtils.dateFormat5(new Date(data.service_end_time * 1000)));
        tvVolunteerDetailProServeAddress.setText(data.address);
        tvVolunteerDetailProRecruitTime.setText(TribeDateUtils.dateFormat5(new Date(data.enroll_time * 1000)) + " 至 " + TribeDateUtils.dateFormat5(new Date(data.enroll_end_time * 1000)));
        tvRewards.setText("服务可获得"+getRewards());
        initRv();
    }

    private String getRewards() {
        float rewardsMin = 0, rewardsMax = 0;
        boolean isFirst = true;
        for (VolunteerProjectBean.PositionsBean e : data.positions
                ) {
            float rew = Float.parseFloat(e.rewards.replaceAll("EOS", "").replaceAll(" ",""));
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
        if (rewardsMin == rewardsMax){
            rewards = CommonUtil.formatPrice(rewardsMin);
        }else {
            rewards = CommonUtil.formatPrice(rewardsMin)+" - "+CommonUtil.formatPrice(rewardsMax);
        }
        return rewards;
    }

    private void initRv() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getCtx()));
        adapter = new VolunteerPositionListAdapter(R.layout.item_volunteer_project_position);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data.positions);
    }


    @OnClick({R.id.btn_volunteer_detail_pro_left, R.id.btn_volunteer_detail_pro_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_volunteer_detail_pro_left:
                ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                break;
            case R.id.btn_volunteer_detail_pro_right:
                PositionChoosePanel choosePanel = new PositionChoosePanel(getCtx(), data);
                choosePanel.show();
                break;
        }
    }
}
