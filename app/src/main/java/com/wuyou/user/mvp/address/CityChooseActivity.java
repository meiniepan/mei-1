package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.CityBean;
import com.wuyou.user.bean.response.CityListResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.AddressApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/8.
 */

public class CityChooseActivity extends BaseActivity {
    @BindView(R.id.city_list)
    RecyclerView recyclerView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CarefreeRetrofit.getInstance().createApi(AddressApis.class).getCityList(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CityListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CityListResponse> cityListResponseBaseResponse) {
                        setData(cityListResponseBaseResponse.data.list);
                    }
                });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_city_choose;
    }

    public void setData(List<CityBean> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(CommonUtil.getRecyclerDivider(this));
        BaseQuickAdapter adapter = new BaseQuickAdapter<CityBean, BaseHolder>(R.layout.item_city) {
            @Override
            protected void convert(BaseHolder helper, CityBean item) {
                helper.setText(R.id.city_item_name, item.city_name);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(Constant.CITY, data.get(position));
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
