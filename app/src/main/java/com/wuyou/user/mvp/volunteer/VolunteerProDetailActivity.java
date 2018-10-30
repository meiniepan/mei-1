package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

/**
 * Created by Solang on 2018/10/29.
 */

public class VolunteerProDetailActivity extends BaseActivity {
    VolunteerProjectBean data;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_volunteer_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.project_detail));
        data = getIntent().getParcelableExtra(Constant.VOLUNTEER_PROJECT);
    }

    public void doApply(View view) {
        participateVolunteerProject();
    }

    public void participateVolunteerProject() {
        String id = data.id + "";
        String org = data.creator;
        String proName = data.name;
        String posName = data.positions.get(0).name;
        EoscDataManager.getIns().participateTimeBank(id, org, proName, posName)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        ToastUtils.ToastMessage(getCtx(), "报名成功！");
                        finish();
                    }
                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        if (message.message.contains("You have enrolled")) {
                            ToastUtils.ToastMessage(getCtx(), "您已经报过名了");
                        } else {
                            ToastUtils.ToastMessage(getCtx(), message.message);
                        }
                        finish();
                    }
                });
    }
}
