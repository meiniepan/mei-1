package com.wuyou.user.mvp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.mvp.serve.ServeCategoryListActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.main_serve_list)
    RecyclerView mainServeList;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getCategoryList(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CategoryListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CategoryListResponse> orderListResponseBaseResponse) {
                        setData(orderListResponseBaseResponse.data.list);
                    }
                });
    }

    @Override
    public void showError(String message, int res) {

    }

    public void setData(List<CategoryParent> data) {
        mainServeList.setLayoutManager(new LinearLayoutManager(mCtx));
        mainServeList.setAdapter(new MainServeAdapter(R.layout.item_main_serve,data,mCtx));
    }
}
