package com.wuyou.user.mvp.score;

import android.os.Bundle;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.adapter.SignInRecordAdapter;
import com.wuyou.user.bean.SignRecordBean;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DELL on 2018/6/6.
 */

public class SignInRecordFragment extends BaseFragment {
    @BindView(R.id.sign_in_record)
    RefreshRecyclerView signInRecord;
    private SignInRecordAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_in_record;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        signInRecord.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(mCtx));
        adapter = new SignInRecordAdapter(R.layout.item_sign_in_record);
        signInRecord.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this::loadMore, signInRecord.getRecyclerView());
    }

    @Override
    protected void loadDataWhenVisible() {
        super.loadDataWhenVisible();
        signInRecord.showProgressView();
        CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getSignInRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "1").put("start_id", "0").buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<SignRecordBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<SignRecordBean>> listResponseBaseResponse) {
                        signInRecord.showContentView();
                        adapter.setNewData(listResponseBaseResponse.data.list);
                        if (adapter.getData().size() == 0) {
                            signInRecord.showEmptyView("暂无签到记录");
                            return;
                        }
                        if (listResponseBaseResponse.data.has_more == 0) {
                            adapter.loadMoreEnd(true);
                        }
                        startId = adapter.getData().get(adapter.getData().size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        signInRecord.showErrorView(getString(R.string.get_sign_record_fail));
                    }
                });
    }

    private String startId;

    private void loadMore() {
        CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                .getSignInRecord(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("flag", "2").put("start_id", startId).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<SignRecordBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<SignRecordBean>> listResponseBaseResponse) {
                        List<SignRecordBean> list = listResponseBaseResponse.data.list;
                        adapter.addData(list);
                        if (listResponseBaseResponse.data.has_more == 0) {
                            adapter.loadMoreEnd(true);
                        }
                        startId = adapter.getData().get(list.size() - 1).id;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        adapter.loadMoreFail();
                    }
                });
    }
}
