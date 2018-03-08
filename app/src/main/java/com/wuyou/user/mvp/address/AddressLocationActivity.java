package com.wuyou.user.mvp.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.AddressLocationListAdapter;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressLocationActivity extends BaseActivity {
    private GeocodeSearch geocodeSearch;
    public AMapLocationClient mLocationClient = null;

    @BindView(R.id.address_location_list)
    RecyclerView addressList;
    private int flag; // 0 选择地址  1 添加地址定位按钮
    private String city;
    private String province;
    private String district;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra(Constant.ADDRESS_LOCATION_FLAG, 0);
        mLocationClient = new AMapLocationClient(this);
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                List<PoiItem> pois = regeocodeResult.getRegeocodeAddress().getPois();
                setListData(pois);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    city = aMapLocation.getCity();
                    province = aMapLocation.getProvince();
                    district = aMapLocation.getDistrict();
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 200, GeocodeSearch.AMAP);
                    geocodeSearch.getFromLocationAsyn(query);
                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                            + aMapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            }
        });
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        showLoadingDialog();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_location_address;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    public void setListData(List<PoiItem> listData) {
        dismissDialog();
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressList.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(this, 0.5f), getResources().getColor(R.color.tint_bg)));
        AddressLocationListAdapter adapter = new AddressLocationListAdapter(R.layout.item_address_location, listData);
        addressList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> setClickResult(listData.get(position)));
    }

    private void setClickResult(PoiItem poiItem) {
        if (flag == 0) {
            EventBus.getDefault().post(new AddressEvent(poiItem));
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constant.ADDRESS_RESULT, poiItem);
            poiItem.setProvinceName(province);
            poiItem.setCityName(city);
            poiItem.setAdName(district);
            setResult(200, intent);
        }
        finish();
    }
}
