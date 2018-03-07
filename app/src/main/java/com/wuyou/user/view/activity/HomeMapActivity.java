package com.wuyou.user.view.activity;

import android.os.Bundle;

import com.wuyou.user.R;

/**
 * Created by DELL on 2018/3/7.
 */

public class HomeMapActivity extends BaseActivity {
//    @BindView(R.id.map_view)
//    MapView mapView;
//    private AMap mAMap;

    @Override
    protected void bindView(Bundle savedInstanceState) {
//        mapView.onCreate(savedInstanceState);
//        mAMap = mapView.getMap();
//        mAMap.setLocationSource(this);// 设置定位监听
//        geocodeSearch = new GeocodeSearch(this);
//        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                List<PoiItem> pois = regeocodeResult.getRegeocodeAddress().getPois();
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//        deactivate();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        mapView.onDestroy();
//        super.onDestroy();
//    }
//
//
//    public AMapLocationClient mLocationClient = null;
//    private OnLocationChangedListener mListener;
//    private GeocodeSearch geocodeSearch;
//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//        if (mLocationClient == null) {
//            mLocationClient = new AMapLocationClient(this);
//
//            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//            // 设置定位监听
//            mLocationClient.setLocationListener(aMapLocation -> {
//                if (mListener != null && aMapLocation != null) {
//                    if (aMapLocation.getErrorCode() == 0) {
//                        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 200, GeocodeSearch.AMAP);
//                        geocodeSearch.getFromLocationAsyn(query);
//                    } else {
//                        String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
//                                + aMapLocation.getErrorInfo();
//                        Log.e("AmapErr", errText);
//                    }
//                }
//            });
//            // 设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            // 只是为了获取当前位置，所以设置为单次定位
//            mLocationOption.setOnceLocation(true);
//            // 设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.startLocation();
//
//        }
//    }
//
//    @Override
//    public void deactivate() {
//
//    }
}
