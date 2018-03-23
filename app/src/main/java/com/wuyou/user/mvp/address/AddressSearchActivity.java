package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressLocationListAdapter;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by DELL on 2018/3/9.
 */

public class AddressSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {
    @BindView(R.id.address_search)
    EditText addressSearch;
    @BindView(R.id.address_search_list)
    RecyclerView addressSearchList;
    @BindView(R.id.address_search_status)
    StatusLayout addressSearchStatus;

    @Override
    protected void bindView(Bundle savedInstanceState) {

        addressSearch.setOnEditorActionListener((v, actionId, event) -> {
            searchPoi();
            return false;
        });
    }

    private void searchPoi() {
        String keyWord = addressSearch.getText().toString().trim();
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "商务住宅", "北京");
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
        addressSearchStatus.showProgressView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address_search;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        int flag = getIntent().getIntExtra(Constant.ADDRESS_SEARCH_FLAG, 0); // 0 add address search 1  home search
        addressSearchList.setLayoutManager(new LinearLayoutManager(this));
        addressSearchStatus.showContentView();
        AddressLocationListAdapter adapter = new AddressLocationListAdapter(R.layout.item_address_location, poiResult.getPois());
        addressSearchList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            setClickResult(poiResult.getPois().get(position),flag);
        });
    }

    private void setClickResult(PoiItem poiItem, int flag) {
        if (flag == 0){
            Intent intent = new Intent();
            intent.putExtra(Constant.POI_RESULT,poiItem);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            EventBus.getDefault().post(new AddressEvent(poiItem));
            finish();
            AppManager.getAppManager().finishActivity(AddressActivity.class);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
