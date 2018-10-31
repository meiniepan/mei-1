package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VolunteerPositionListAdapter;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.panel.EnvironmentChoosePanel;
import com.wuyou.user.view.widget.panel.PositionChoosePanel;

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
    @BindView(R.id.tv_volunteer_detail_pro_haha)
    TextView tvVolunteerDetailProHaha;
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
    }

    private void initUI() {
        GlideUtils.loadRoundCornerImage(getCtx(), Constant.IPFS_URL + data.logofile, ivVolunteerDetailLogo);
        tvVolunteerDetailProName.setText(data.name);
        tvVolunteerDetailProServeTime.setText(data.service_time.split("T")[0] + " 至 " + data.service_end_time.split("T")[0]);
        tvVolunteerDetailProServeAddress.setText(data.address);
        tvVolunteerDetailProRecruitTime.setText(data.enroll_time.split("T")[0] + " 至 " + data.enroll_end_time.split("T")[0]);
        initRv();
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
                break;
            case R.id.btn_volunteer_detail_pro_right:
                PositionChoosePanel choosePanel = new PositionChoosePanel(getCtx(),data);
                choosePanel.show();
                break;
        }
    }
}
