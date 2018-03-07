package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/3/7.
 */

public class AddressActivity extends BaseActivity {
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

    @Override
    protected void bindView(Bundle savedInstanceState) {

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
                intent.setClass(getCtx(), AddressManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.address_current_location:
                intent.setClass(getCtx(), AddressLocationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
