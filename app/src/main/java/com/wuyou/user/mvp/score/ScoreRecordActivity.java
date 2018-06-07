package com.wuyou.user.mvp.score;

import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindView(Bundle savedInstanceState) {
        scoreRecordList.getStatusLayout().setEmptyContentViewMargin(0, -200, 0, 0);
        flag = getIntent().getIntExtra(Constant.SCORE_FLAG, 0);
        adapter = new ScoreRecordAdapter(R.layout.item_score_record, flag);
        scoreRecordList.setAdapter(adapter);
        scoreRecordList.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));
        if (flag == 0) {
            scoreRecordTitle.setText(R.string.gained_score_record);
            scoreRecordFlag.setText(R.string.total_gained_score);
            getObtainRecord();
            scoreRecordList.setRefreshAction(this::getObtainRecord);
            adapter.setOnLoadMoreListener(this::getMoreObtain, scoreRecordList.getRecyclerView());
        } else {
            scoreRecordTitle.setText(R.string.consume_score_record);
            scoreRecordFlag.setText(R.string.total_consume_score);
            scoreRecordList.showEmptyView("暂无消耗积分记录");
        }
        scoreAmount.setText(getIntent().getLongExtra(Constant.SCORE_AMOUNT, 0) + "");



    }

    public void getObtainRecord() {
        scoreRecordList.getRefreshLayout().setRefreshing(false);
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
        scoreRecordList.showContentView();
        adapter.setNewData(data);
        if (data == null || data.size() == 0) {
            scoreRecordList.showEmptyView("暂无积分记录");
            return;
        }
        obtainStartId = data.get(data.size() - 1).id;
    }

    private String obtainStartId;

    public void getMoreObtain() {
        CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getScoreRecordList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("start_id", obtainStartId).put("flag", "2").buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ScoreRecordBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ScoreRecordBean>> listResponseBaseResponse) {
                        List<ScoreRecordBean> list = listResponseBaseResponse.data.list;
                        adapter.addData(list);
                        if (listResponseBaseResponse.data.has_more == 0) {
                            adapter.loadMoreEnd(true);
                        }
                        obtainStartId = list.get(list.size() - 1).id;
                    }
                });
    }
}
