package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressListAdapter;
import com.wuyou.user.data.remote.AddressBean;
import com.wuyou.user.data.remote.response.AddressListResponse;
import com.wuyou.user.mvp.address.AddressAddActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.AddressApis;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/13.
 */

public class OrderAddressActivity extends BaseActivity {

    @BindView(R.id.address_order_list)
    RecyclerView addressOrderList;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.address_choose);
        setTitleIconText(R.string.add_address, v -> addAddress());
        addressOrderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(AddressApis.class)
                .getAddressList(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AddressListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AddressListResponse> addressListResponseBaseResponse) {
                        setData(addressListResponseBaseResponse.data.list);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_address;
    }


    public void addAddress() {
        Intent intent = new Intent(getCtx(), AddressAddActivity.class);
        startActivity(intent);
    }

    public void setData(ArrayList<AddressBean> data) {
        AddressListAdapter adapter = new AddressListAdapter(R.layout.item_address_list, data);
        addressOrderList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(Constant.ADDRESS_RESULT, data.get(position));
            setResult(204, intent);
            finish();
        });
    }
}
