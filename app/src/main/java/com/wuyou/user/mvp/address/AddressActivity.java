package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressListAdapter;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressActivity extends BaseActivity<AddressConstract.View, AddressConstract.Presenter> implements AddressConstract.View {
    @BindView(R.id.address_title)
    TextView addressTitle;
    @BindView(R.id.address_list)
    NewRefreshRecyclerView addressList;
    @BindView(R.id.address_status)
    StatusLayout addressStatus;
    @BindView(R.id.address_manager)
    TextView addressManager;
    @BindView(R.id.address_current_location)
    TextView addressCurrentLocation;
    @BindView(R.id.address_empty_view)
    RelativeLayout addressEmptyView;

    private AddressListAdapter adapter;
    private ArrayList<AddressBean> addressData;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        addressList.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressListAdapter(R.layout.item_address_list);
        addressList.setAdapter(adapter);
        addressList.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(this, 0.5f), getResources().getColor(R.color.tint_bg)));
    }

    @Override
    protected AddressConstract.Presenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address;
    }


    @OnClick({R.id.address_manager, R.id.address_current_location})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.address_manager:
                if (addressData == null) return;
                intent.setClass(getCtx(), AddressManagerActivity.class);
                intent.putParcelableArrayListExtra(Constant.ADDRESS_LIST, addressData);
                startActivity(intent);
                break;
            case R.id.address_current_location:
                intent.setClass(getCtx(), AddressLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressStatus.showProgressView();
        mPresenter.getAddress();
    }

    @Override
    public void getAddressSuccess(AddressListResponse list) {
        addressStatus.showContentView();
        addressData = new ArrayList<>();
        addressData.addAll(list.list);
        adapter.setNewData(addressData);
        if (adapter.getData().size() == 0) {
            addressEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateSuccess(AddressBean data) {

    }

    @Override
    public void deleteSuccess(int position) {

    }


    @Override
    public void addSuccess(AddressBean bean) {
    }

    @Override
    public void showError(String message, int res) {
        addressStatus.showErrorView(message);
    }
}
