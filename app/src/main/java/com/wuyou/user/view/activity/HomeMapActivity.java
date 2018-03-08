package com.wuyou.user.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.wuyou.user.R;

import butterknife.BindView;

/**
 * Created by hjn on 2018/3/7.
 */

public class HomeMapActivity extends BaseActivity implements LocationSource, AMap.OnMapClickListener {
    @BindView(R.id.map_view)
    MapView mapView;
    private AMap mAMap;

    private MarkerOptions markerOption = null;
    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private Marker centerMarker;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mAMap = mapView.getMap();
        mAMap = mapView.getMap();
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
        mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));

        markerOption = new MarkerOptions().draggable(true);
        markerOption.icon(ICON_RED);
        centerMarker = mAMap.addMarker(markerOption);

        setUpMap();
    }

    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(255, 255, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(3);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(0xff627db9);
        myLocationStyle.showMyLocation(true);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    public AMapLocationClient mLocationClient = null;
    private LocationSource.OnLocationChangedListener mListener;
    private GeocodeSearch geocodeSearch;
    private LatLng centerLatLng;

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
//        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);

            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (mListener != null && aMapLocation != null) {
                        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                            centerLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));
                            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 200, GeocodeSearch.AMAP);
                            geocodeSearch.getFromLocationAsyn(query);


                        } else {
                            String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                                    + aMapLocation.getErrorInfo();
                            Log.e("AmapErr", errText);
                        }
                    }
                }
            });
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }
}
