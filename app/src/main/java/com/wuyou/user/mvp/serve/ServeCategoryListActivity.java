package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.bean.response.ServeListResponse;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.ColorTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServeCategoryListActivity extends BaseActivity<ServeContract.View, ServeContract.Presenter> implements ServeContract.View {
    @BindView(R.id.serve_category)
    TextView serveCategory;
    @BindView(R.id.serve_category_sort_price_mark)
    ImageView sortPriceMark;
    @BindView(R.id.serve_category_list)
    RecyclerView serveList;
    @BindView(R.id.serve_category_status)
    StatusLayout serveCategoryStatus;
    @BindView(R.id.serve_category_default)
    ColorTextView serveCategoryDefault;
    @BindView(R.id.serve_category_price)
    ColorTextView serveCategoryPrice;
    @BindView(R.id.serve_category_sale)
    ColorTextView serveCategorySale;
    private ServeListAdapter adapter;
    private String categoryId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        serveCategoryDefault.setSelected(true);
        Intent i = getIntent();
        categoryId = i.getStringExtra(Constant.CATEGORY_ID);
        serveCategory.setText(i.getStringExtra(Constant.CATEGORY_NAME));
        serveList.setLayoutManager(new LinearLayoutManager(getCtx()));
        serveList.addItemDecoration(CommonUtil.getRecyclerDivider(this));
        adapter = new ServeListAdapter(getCtx(), R.layout.item_serve_list);
        serveList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getCtx(), ServeDetailActivity.class);
            ServeBean bean = (ServeBean) adapter.getData().get(position);
            intent.putExtra(Constant.SERVE_ID, bean.service_id);
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> mPresenter.getServeMore(), serveList);
        serveCategoryStatus.showProgressView();
        serveCategoryStatus.setErrorAction(v -> mPresenter.getServe(categoryId, 0, 0));
        mPresenter.getServe(categoryId, 0, 0);
        findViewById(R.id.serve_list_sort).setOnClickListener(v -> ToastUtils.ToastMessage(getCtx(), R.string.no_function));
    }

    @Override
    protected ServeContract.Presenter getPresenter() {
        return new ServePresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_category_list;
    }

    public void fastCreate(View view) {
        if (!checkUser(this)) return;
        Intent intent = new Intent(getCtx(), FastCreateActivity.class);
        intent.putExtra(Constant.CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    @Override
    public void getServeSuccess(ServeListResponse response) {
        serveCategoryStatus.showContentView();
        adapter.setNewData(response.list);
        if (adapter.getData().size() == 0) {
            serveCategoryStatus.showEmptyView("尚无当前种类服务");
            return;
        }
        if (response.has_more == 0) adapter.loadMoreEnd(true);
    }

    @Override
    public void loadMore(ServeListResponse data) {
        adapter.addData(data.list);
        if (data.has_more == 0) {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void loadMoreFail(String displayMessage, int code) {
        adapter.loadMoreFail();
    }

    @Override
    public void showError(String message, int res) {
        serveCategoryStatus.showErrorView(message);
    }


    private int sortStatus = 0;  // 0默认  1 价格  2 销量
    private int priceStatus = 0; //0 降序  1升序

    @OnClick({R.id.serve_category_sort_default, R.id.serve_category_sort_price, R.id.serve_category_sort_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.serve_category_sort_default:
                if (sortStatus != 0) {
                    sortStatus = 0;
                    sortPriceMark.setImageResource(R.mipmap.arrow_black_down);
                    serveCategoryStatus.showProgressView();
                    mPresenter.getServe(categoryId, sortStatus, 0);
                }
                break;
            case R.id.serve_category_sort_price:
                sortStatus = 1;
                if (priceStatus == 0) {
                    priceStatus = 1;
                    sortPriceMark.setImageResource(R.mipmap.sort_top);
                } else {
                    priceStatus = 0;
                    sortPriceMark.setImageResource(R.mipmap.sort_down);
                }
                serveCategoryStatus.showProgressView();
                mPresenter.getServe(categoryId, sortStatus, priceStatus);
                break;
            case R.id.serve_category_sort_sale:
                if (sortStatus != 2) {
                    sortStatus = 2;
                    sortPriceMark.setImageResource(R.mipmap.arrow_black_down);
                    serveCategoryStatus.showProgressView();
                    mPresenter.getServe(categoryId, sortStatus, 0);
                }
                break;
        }
        serveCategoryDefault.setSelected(sortStatus == 0);
        serveCategoryPrice.setSelected(sortStatus == 1);
        serveCategorySale.setSelected(sortStatus == 2);
    }
}
