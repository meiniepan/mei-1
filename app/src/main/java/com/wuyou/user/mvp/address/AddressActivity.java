package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressListAdapter;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;

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
        adapter.setOnItemClickListener((adapter, view, position) -> {
            AddressBean addressBean = (AddressBean) adapter.getData().get(position);
            PoiItem item = new PoiItem("", new LatLonPoint(addressBean.lat, addressBean.lng), addressBean.area, addressBean.address);
            EventBus.getDefault().post(new AddressEvent(item));
            finish();
        });
        if (null == CarefreeDaoSession.getInstance().getUserInfo()) {
            addressStatus.showEmptyView("您还未登录，请先登录");
            addressStatus.setEmptyAction(v -> {
                Intent intent = new Intent(getCtx(), LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected AddressConstract.Presenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address;
    }


    @OnClick({R.id.address_manager, R.id.address_current_location,R.id.address_search})
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
            case R.id.address_search:
                intent.setClass(getCtx(),AddressSearchActivity.class);
                intent.putExtra(Constant.ADDRESS_SEARCH_FLAG,1);
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
