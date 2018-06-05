package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ScoreRecordAdapter;
import com.wuyou.user.bean.ScoreRecordBean;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Solang on 2018/6/1.
 */

public class ScoreRecordActivity extends BaseActivity {
    @BindView(R.id.score_record_title)
    TextView scoreRecordTitle;
    @BindView(R.id.score_record_flag)
    TextView scoreRecordFlag;
    @BindView(R.id.score_amount)
    TextView scoreAmount;
    @BindView(R.id.score_record_list)
    RefreshRecyclerView scoreRecordList;
    private int flag;
    private ScoreRecordAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra(Constant.SCORE_FLAG, 0);
        adapter = new ScoreRecordAdapter(R.layout.item_score_record);
        scoreRecordList.setAdapter(adapter);
        scoreRecordList.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));
        scoreRecordList.setRefreshAction(() -> getObtainRecord());
        if (flag == 0) {
            scoreRecordTitle.setText(R.string.gained_score_record);
            scoreRecordFlag.setText(R.string.total_gained_score);
            getObtainRecord();
        } else {
            scoreRecordTitle.setText(R.string.consume_score_record);
            scoreRecordFlag.setText(R.string.total_consume_score);
        }

        scoreRecordList.showEmptyView("暂无积分记录");
    }

    public void getObtainRecord() {
        CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", "0").buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ScoreRecordBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ScoreRecordBean>> listResponseBaseResponse) {
                        ListResponse<ScoreRecordBean> data = listResponseBaseResponse.data;
                        setData(data.list);
                        if (data.has_more == 0) {
                            adapter.loadMoreEnd(true);
                        }
                    }
                });
    }

    public void setData(List<ScoreRecordBean> data) {
        adapter.setNewData(data);
    }
}
