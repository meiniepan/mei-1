package com.wuyou.user.mvp.volunteer;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/10/26.
 */

public class TimeBankMainFragment extends BaseFragment {
    @BindView(R.id.block_main_search)
    EditText blockMainSearch;
    @BindView(R.id.time_bank_main_list)
    RecyclerView timeBankMainList;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_time_bank_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        EoscDataManager.getIns().getTable("samkunnbanb1 ", Constant.EOS_TIME_BANK, "task")
                .compose(RxUtil.switchSchedulers())
                .map(s -> {
                    VolunteerProjectBean projectBean = new GsonBuilder().create().fromJson(s, VolunteerProjectBean.class);
                    Log.e("Carefree", "apply: " + s);
                    return projectBean.rows;
                })
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<List<VolunteerProjectBean.RowsBean>>() {
                    @Override
                    public void onSuccess(List<VolunteerProjectBean.RowsBean> rowsBeans) {
                        Log.e("Carefree", "onSuccess: " + rowsBeans.size());
                    }
                });
    }


    @OnClick({R.id.time_bank_main_project, R.id.time_bank_main_map, R.id.time_bank_main_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_bank_main_project:
            case R.id.time_bank_main_more:
                break;
            case R.id.time_bank_main_map:
                break;
        }
    }
}
