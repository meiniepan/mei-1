package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.bean.ServeLevelBean;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class FastCreateActivity extends BaseActivity {
    @BindView(R.id.fast_serve_level)
    RecyclerView recyclerView;
    @BindView(R.id.fast_create)
    Button button;
    private String categoryId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        categoryId = getIntent().getStringExtra(Constant.CATEGORY_ID);
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getFastServeLevel(categoryId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ServeLevelBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ServeLevelBean>> baseResponse) {
                        setData(baseResponse.data.list);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_fast_create;
    }

    public void fastCreateOrder(View view) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeDetail(bean.service_id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeDetailBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeDetailBean> serveDetailBeanBaseResponse) {
                        Intent intent = new Intent(getCtx(), NewOrderActivity.class);
                        intent.putExtra(Constant.SERVE_BEAN, serveDetailBeanBaseResponse.data);
                        startActivity(intent);
                    }
                });
    }

    private ServeLevelBean bean;

    public void setData(List<ServeLevelBean> data) {
        if (data == null || data.size() == 0) {
            ToastUtils.ToastMessage(getCtx(), "当前服务没有服务等级");
            return;
        }
        button.setEnabled(true);
        bean = data.get(0);
        bean.selected = true;
        FastServeAdapter adapter = new FastServeAdapter(R.layout.item_fast_serve, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            adapter.setCheckPos(position);
            bean = data.get(position);
        });
    }
}
