package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressListAdapter;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.util.CommonUtil;
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
    private AddressListAdapter adapter;
    private ArrayList<AddressBean> addressData;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setUpStatus();
        addressList.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressListAdapter(R.layout.item_address_list);
        addressList.setAdapter(adapter);
        addressList.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));
        adapter.setOnItemClickListener((adapter, view, position) -> {
            AddressBean addressBean = (AddressBean) adapter.getData().get(position);
            PoiItem item = new PoiItem("", new LatLonPoint(addressBean.lat, addressBean.lng), addressBean.area, addressBean.address);
            EventBus.getDefault().post(new AddressEvent(item));
            finish();
        });

        if (null == CarefreeDaoSession.getInstance().getUserId()) {
            addressStatus.showLoginView(getString(R.string.no_login));
        }
    }

    private void setUpStatus() {
        addressStatus.getErrorActView().setText(getString(R.string.reload));
        addressStatus.setErrorAction(v -> mPresenter.getAddress());

        addressStatus.setLoginAction(v -> {
            Intent intent = new Intent(getCtx(), LoginActivity.class);
            startActivity(intent);
        });
        addressStatus.getLoginActView().setText(R.string.login_now);

        addressStatus.getEmptyActView().setText(R.string.add_address);
        addressStatus.setEmptyAction(v -> {
            Intent intent = new Intent(getCtx(), AddressAddActivity.class);
            startActivity(intent);
        });
    }

    private void updateAddressAsDefault(AddressBean bean) {
        showLoadingDialog();
        bean.is_default = 1;
        mPresenter.updateAddress(bean.id, bean);
    }

    @Override
    protected AddressConstract.Presenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address;
    }


    @OnClick({R.id.address_manager, R.id.address_current_location, R.id.address_search})
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
                intent.setClass(getCtx(), AddressSearchActivity.class);
                intent.putExtra(Constant.ADDRESS_SEARCH_FLAG, 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == CarefreeDaoSession.getInstance().getUserInfo()) return;
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
            addressStatus.showEmptyView(getString(R.string.no_address));
        } else {
            CarefreeDaoSession.getInstance().saveDefaultAddress(list.list.get(0));
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
