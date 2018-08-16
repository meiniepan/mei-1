package com.wuyou.user.mvp.score;

import android.os.Bundle;

import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.adapter.SignInRecordAdapter;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.CarefreeRecyclerView;

import butterknife.BindView;

/**
 * Created by DELL on 2018/6/6.
 */

public class SignInRecordFragment extends BaseFragment {
    @BindView(R.id.sign_in_record)
    CarefreeRecyclerView signInRecord;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_in_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        SignInRecordAdapter adapter = new SignInRecordAdapter(R.layout.item_sign_in_record);
        signInRecord.setAdapter(adapter);
        signInRecord.setLoadMoreListener(() -> signInRecord.getDataMore(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getSignInRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "2").put("start_id", signInRecord.startId).buildGet())));

        signInRecord.getRecyclerView().setErrorAction(v -> getData());
    }

    private void getData() {
        signInRecord.initData(CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getSignInRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "1").put("start_id", "0").buildGet()));
    }

    @Override
    protected void loadDataWhenVisible() {
        getData();
    }
}
