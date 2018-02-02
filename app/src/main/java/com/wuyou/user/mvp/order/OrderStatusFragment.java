package com.wuyou.user.mvp.order;

import android.content.Intent;
import android.os.Bundle;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;
import com.wuyou.user.mvp.login.PhoneInputActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.recyclerHelper.NewRefreshRecyclerView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderStatusFragment extends BaseFragment {
    @BindView(R.id.order_list)
    NewRefreshRecyclerView orderList;
    @BindView(R.id.order_list_layout)
    StatusLayout orderListLayout;
    private int type;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_status_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRootView.findViewById(R.id.login).setOnClickListener(v ->
                {
                    if (CarefreeApplication.getInstance().getUserInfo() != null) {
                        ToastUtils.ToastMessage(getContext(), "login already");
                    } else {
                        Intent view = new Intent(mCtx, PhoneInputActivity.class);
                        startActivity(view);
                    }
                }
        );
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void showError(String message, int res) {

    }
}
