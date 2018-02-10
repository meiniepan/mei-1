package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.bean.response.FastLevelResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CustomNestRadioGroup;

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
    private String categoryId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        categoryId = getIntent().getStringExtra(Constant.CATEGORY_ID);
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getFastServeLevel(categoryId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<FastLevelResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<FastLevelResponse> baseResponse) {
                        setData(baseResponse.data.list);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_fast_create;
    }

    public void createOrder(View view) {
        Intent intent = new Intent(getCtx(), NewOrderActivity.class);
        intent.putExtra(Constant.SERVE_BEAN, bean);
        startActivity(intent);
    }

    private ServeDetailBean bean;

    public void setData(List<ServeDetailBean> data) {
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
