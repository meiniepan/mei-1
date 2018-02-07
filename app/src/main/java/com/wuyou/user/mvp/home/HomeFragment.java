package com.wuyou.user.mvp.home;

import android.content.Intent;
import android.os.Bundle;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.mvp.serve.ServeCategoryListActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.view.fragment.BaseFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HomeFragment extends BaseFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getCategotyList(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {

                    }
                });

        mRootView.findViewById(R.id.v1)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(mCtx, ServeCategoryListActivity.class);
                    startActivity(intent);
                });
    }

    @Override
    public void showError(String message, int res) {

    }
}
