package com.wuyou.user.mvp.wallet;

import android.os.Bundle;

import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ActivityRecordAdapter;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import butterknife.BindView;

/**
 * Created by DELL on 2018/9/17.
 */

public class ActivityRecordActivity extends BaseActivity {
    @BindView(R.id.activity_record)
    CarefreeRecyclerView activityRecord;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.my_activity_record);
        activityRecord.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));
        ActivityRecordAdapter adapter = new ActivityRecordAdapter();
        activityRecord.setAdapter(adapter);
        activityRecord.setLoadMoreListener(() -> activityRecord.getOrderDataMore(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getActivityRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "2").put("start_id", activityRecord.startId).buildGet())));
        activityRecord.initOrderData(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getActivityRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "1").put("start_id", "0").buildGet()));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_activity_record;
    }
}
