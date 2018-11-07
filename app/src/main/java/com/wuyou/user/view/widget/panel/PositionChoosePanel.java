package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.VolunteerPositionChooseAdapter;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.mvp.kyc.KycAuthActivity;
import com.wuyou.user.mvp.volunteer.ApplySuccessActivity;
import com.wuyou.user.mvp.volunteer.VolunteerProDetailActivity;
import com.wuyou.user.util.RxUtil;

import butterknife.ButterKnife;

/**
 * Created by Solang on 2018/10/30.
 */

public class PositionChoosePanel extends Dialog {
    VolunteerProjectBean data;
    String posName;

    public PositionChoosePanel(Context context, VolunteerProjectBean data) {
        super(context, R.style.bottom_dialog);
        this.data = data;
        initView();
    }

    private void initView() {
        for (VolunteerProjectBean.PositionsBean e : data.positions
                ) {
            e.isChosen = false;
        }
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.position_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_position_panel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        VolunteerPositionChooseAdapter adapter = new VolunteerPositionChooseAdapter(R.layout.item_position_panel);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data.positions);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                for (VolunteerProjectBean.PositionsBean e : data.positions
                        ) {
                    e.isChosen = false;
                }
                data.positions.get(i).isChosen = true;
                posName = data.positions.get(i).name;
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.tv_position_auth).setOnClickListener(v -> {
            navigateToTrace(data.name);
        });
        findViewById(R.id.tv_position_apply).setOnClickListener(v -> {
            participateVolunteerProject();
        });
    }

    private void navigateToTrace(String name) {
        Intent intent = new Intent(getContext(), KycAuthActivity.class);
        intent.putExtra(Constant.TRACE_KEY_WORD, name);
        getContext().startActivity(intent);
    }

    public void participateVolunteerProject() {
        if (TextUtils.isEmpty(posName)) {
            ToastUtils.ToastMessage(getContext(), "请先选择岗位");
            return;
        }
        LoadingDialog.getInstance().show(getContext(), "", false);
        String id = data.id + "";
        String org = data.creator;
        String proName = data.name;
        EoscDataManager.getIns().participateTimeBank(id, org, proName, posName)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        getContext().startActivity(new Intent(getContext(), ApplySuccessActivity.class));
                        AppManager.getAppManager().finishActivity(VolunteerProDetailActivity.class);
                        dismiss();
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        if (message.message.contains("You have enrolled")) {
                            ToastUtils.ToastMessage(getContext(), "您已经报过名了");
                        } else if (message.message.contains("enrolled complete")) {
                            ToastUtils.ToastMessage(getContext(), "报名人数已满");
                        } else {
                            ToastUtils.ToastMessage(getContext(), message.message);
                        }
                        dismiss();
                    }
                });
    }
}
