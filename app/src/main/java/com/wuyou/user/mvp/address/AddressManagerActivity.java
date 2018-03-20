package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressListAdapter;
import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.response.AddressListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressManagerActivity extends BaseActivity<AddressConstract.View, AddressConstract.Presenter> implements AddressConstract.View {
    @BindView(R.id.address_manager_list)
    RecyclerView addressManagerList;
    @BindView(R.id.address_manager_add)
    TextView addressManagerAdd;
    @BindView(R.id.address_manager_status)
    StatusLayout addressManagerStatus;

    private AddressListAdapter adapter;
    private int updatePosition;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<AddressBean> list = getIntent().getParcelableArrayListExtra(Constant.ADDRESS_LIST);
        addressManagerList.setLayoutManager(new LinearLayoutManager(this));
        addressManagerList.addItemDecoration(CommonUtil.getRecyclerDivider(this));
        adapter = new AddressListAdapter(R.layout.item_address_list);
        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            showDeleteDialog(position, (AddressBean) adapter.getData().get(position));
            return false;
        });
        addressManagerList.setAdapter(adapter);
        adapter.setNewData(list);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            showDeletePop(view, (AddressBean) adapter.getData().get(position));
            updatePosition = position;
            Intent intent = new Intent(getCtx(), AddressAddActivity.class);
            intent.putExtra(Constant.ADDRESS_EDIT_FLAG, 1);
            intent.putExtra(Constant.ADDRESS_BEAN, (AddressBean) adapter.getData().get(position));
            startActivityForResult(intent, 201);
        });
        if (list.size() == 0) {
            addressManagerStatus.showEmptyView(getString(R.string.no_address));
        }
    }


    private void showDeletePop(View view, AddressBean bean) {
    }

    private void updateAddressAsDefault(AddressBean bean) {
        showLoadingDialog();
        bean.is_default = 1;
        mPresenter.updateAddress(bean.id, bean);
    }

    private void showDeleteDialog(int pos, AddressBean bean) {
        new CustomAlertDialog.Builder(getCtx()).setTitle("提示").setMessage("您确定要删除该地址？")
                .setPositiveButton("删除", (dialog, which) ->
                        mPresenter.deleteAddress(pos, bean.id)).setNegativeButton("取消", null).create().show();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_manager_address;
    }


    @OnClick({R.id.address_manager_add})
    public void onViewClicked() {
        Intent intent = new Intent(getCtx(), AddressAddActivity.class);
        startActivityForResult(intent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 203) {//添加地址成功
            AddressBean addressBean = data.getParcelableExtra(Constant.ADDRESS_BEAN);
            adapter.addData(0, addressBean);
            addressManagerStatus.showContentView();
        } else if (resultCode == 204) { //编辑地址成功
            AddressBean addressBean = data.getParcelableExtra(Constant.ADDRESS_BEAN);
            adapter.setData(updatePosition, addressBean);
        } else if (resultCode == 205) {// 删除成功
            adapter.remove(updatePosition);
            if (adapter.getData().size() == 0) {
                addressManagerStatus.showEmptyView(getString(R.string.no_address));
            }
        }
    }

    private void showPoP(View v, int i) {
        View view = View.inflate(getCtx(), R.layout.item_pop, null);
        final PopupWindow popupWindow = new PopupWindow(CommonUtil.getScreenWidth(getCtx()) - 40, DensityUtils.dip2px(getCtx(), 80));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.MainPopupWindowAnimationScale);
        if (i < CommonUtil.getScreenHeight(getCtx()) / 2) {
            view.setBackgroundResource(R.mipmap.pop_bg_bottom);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i + DensityUtils.dip2px(getCtx(), 50));
        } else {
            view.setBackgroundResource(R.mipmap.pop_bg_top);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i - DensityUtils.dip2px(getCtx(), 50));
        }
        CommonUtil.backgroundAlpha(this, 0.6f);
        popupWindow.setOnDismissListener(() -> CommonUtil.backgroundAlpha(AddressManagerActivity.this, 1f));

        view.findViewById(R.id.main_item_delete).setOnClickListener(v1 -> popupWindow.dismiss());
    }


    @Override
    protected AddressConstract.Presenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    public void getAddressSuccess(AddressListResponse list) {
        adapter.setNewData(list.list);
        if (adapter.getData().size() == 0) {
            addressManagerStatus.showEmptyView(getString(R.string.no_address));
        }
    }

    @Override
    public void updateSuccess(AddressBean data) {
        CarefreeDaoSession.getInstance().saveDefaultAddress(data);
    }

    @Override
    public void deleteSuccess(int position) {
        adapter.remove(position);
        if (adapter.getData().size() == 0) {
            addressManagerStatus.showEmptyView(getString(R.string.no_address));
        }
    }

    @Override
    public void addSuccess(AddressBean bean) {

    }

    @Override
    public void showError(String message, int res) {
        addressManagerStatus.showErrorView(getString(R.string.connect_fail));
    }
}
