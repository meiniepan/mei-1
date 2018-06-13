package com.wuyou.user.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.ServeSitesAdapter;
import com.wuyou.user.bean.ServeSites;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.GpsUtils;
import com.wuyou.user.view.widget.panel.SingleBottomChoosePanel;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/3/7.
 */

public class HomeMapActivity extends BaseActivity implements LocationSource, AMap.OnMapClickListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_location)
    ImageButton mapLocation;
    @BindView(R.id.map_around)
    ImageButton mapAround;
    @BindView(R.id.map_guide)
    ImageButton mapGuide;
    @BindView(R.id.map_around_spot)
    RecyclerView mapAroundSpot;
    @BindView(R.id.map_layout)
    RelativeLayout mapControlLayout;
    @BindView(R.id.site_layout)
    View siteLayout;
    @BindView(R.id.site_name)
    TextView siteName;
    @BindView(R.id.site_address)
    TextView siteAddress;
    @BindView(R.id.site_time)
    TextView siteTime;
    private AMap mAMap;

    private MarkerOptions markerOption = null;
    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory.fromResource(R.mipmap.red_mark);
    private ServeSites serveSite;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        ObjectAnimator.ofFloat(mapControlLayout, "translationY", DensityUtils.dip2px(this, 200)).setDuration(0).start();
        initMap(savedInstanceState);
    }

    private void initData() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class)
                .getServeSites(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .map(listResponseBaseResponse -> {
                    List<ServeSites> serveSites = listResponseBaseResponse.data.list;
                    Collections.sort(serveSites, (o1, o2) -> {
                        LatLng latLng1 = new LatLng(o1.lat, o1.lng);
                        LatLng latLng2 = new LatLng(o2.lat, o2.lng);
                        o1.distance = AMapUtils.calculateLineDistance(latLng1, centerLatLng);
                        o2.distance = AMapUtils.calculateLineDistance(latLng2, centerLatLng);
                        if (o1.distance < o2.distance) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                    return serveSites;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<ServeSites>>() {
                    @Override
                    public void onSuccess(List<ServeSites> serveSites) {
                        setData(serveSites);
                    }
                });
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mAMap = mapView.getMap();
        mAMap = mapView.getMap();
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        setUpMap();
    }

    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(255, 255, 255, 255));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(3);
        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(0xff627db9);
        myLocationStyle.showMyLocation(true);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ServeSites serveSites = (ServeSites) marker.getObject();
                serveSite = serveSites;
                showSiteInfo(serveSites);
                return true;
            }
        });
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("Test", "onMapClick: " + latLng.latitude + "..........." + latLng.longitude);
            }
        });
    }

    private void showSiteInfo(ServeSites serveSites) {
        mapGuide.setVisibility(View.VISIBLE);
        siteLayout.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(mapControlLayout, "translationY", DensityUtils.dip2px(this, 80)).setDuration(0).start();
        siteName.setText(serveSites.name);
        siteAddress.setText(serveSites.address);
        siteTime.setText("营业时间 " + serveSites.open_all_day);
        mapAround.setBackgroundResource(R.mipmap.map_around_normal);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    public AMapLocationClient mLocationClient = null;
    private OnLocationChangedListener mListener;
    private LatLng centerLatLng;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(aMapLocation -> {
                if (mListener != null && aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                        centerLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));
                        initData();
                    }
                }
            });
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            showLoadingDialog();
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (siteLayout.getVisibility() == View.GONE) {
            setSiteListGone();
        }
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

    @OnClick({R.id.map_location, R.id.map_around, R.id.map_guide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_location:
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                break;
            case R.id.map_around:
                setSitesListVisible();
                break;
            case R.id.map_guide:
                startGuide();
                break;
        }
    }

    private void startGuide() {
        if (serveSite == null) return;
        ArrayList<String> list = new ArrayList<>();
        if (CommonUtil.isAppInstalled(this, Constant.GAODE_PACKAGENAME)) {
            list.add(Constant.GAODE_MAP);
        }
        if (CommonUtil.isAppInstalled(this, Constant.TENCENT_PACKAGENAME)) {
            list.add(Constant.TENCENT_MAP);
        }
        if (CommonUtil.isAppInstalled(this, Constant.BAIDU_PACKAGENAME)) {
            list.add(Constant.BAIDU_MAP);
        }
        if (list.size() == 0) {
            ToastUtils.ToastMessage(getCtx(), "检测到您当前无相关地图应用，请先下载安装");
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (list.size() == 1) {
            doGuide(list.get(0));
        } else {
            showChooseDialog(list);
        }
    }

    private void doGuide(String map) {
        if (Constant.GAODE_MAP.equals(map)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("amapuri://route/plan?sourceApplication=" + getResources().getString(R.string.app_name) +
                    "&dname=" + serveSite.name + "&dlat=" + serveSite.lat + "&dlon=" + serveSite.lng + "&dev=0&t=1");
            intent.setData(uri);
            startActivity(intent);

        } else if (Constant.TENCENT_MAP.equals(map)) {
            String baseUrl = "qqmap://map/";
            String drivePlan = "routeplan?type=drive&from=&fromcoord=&to="+serveSite.name+"&tocoord=" + serveSite.lat + "," + serveSite.lng +"&policy=1";
            String tencnetUri = baseUrl + drivePlan + "&referer=" + getResources().getString(R.string.app_name);
            Intent intent;
            try {
                intent = Intent.parseUri(tencnetUri, 0);
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (Constant.BAIDU_MAP.equals(map)) {
            double[] baiduLoc = GpsUtils.gcj02_To_Bd09(serveSite.lat, serveSite.lng);
            try {
                Intent intent = Intent.parseUri("intent://map/direction?" +
                        "destination=latlng:" + baiduLoc[0] + "," + baiduLoc[1] + "|name:" + serveSite.name +
                        "&mode=driving" + "&region=" + "&src=" + getResources().getString(R.string.app_name) +
                        "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void showChooseDialog(ArrayList<String> list) {
        SingleBottomChoosePanel panel = new SingleBottomChoosePanel(this, list);
        panel.setServeSite(serveSite);
        panel.show();
    }

    private void setSiteListGone() {
        mapAround.setBackgroundResource(R.mipmap.map_around_normal);
        ObjectAnimator.ofFloat(mapControlLayout, "translationY", DensityUtils.dip2px(this, 200)).setDuration(300).start();
    }

    private void setSitesListVisible() {
        mapAround.setBackgroundResource(R.mipmap.map_around_pressed);
        ObjectAnimator.ofFloat(mapControlLayout, "translationY", DensityUtils.dip2px(this, 0)).setDuration(300).start();
        siteLayout.setVisibility(View.GONE);
    }

    public void setData(List<ServeSites> data) {
        mapAroundSpot.setLayoutManager(new LinearLayoutManager(this));
        ServeSitesAdapter adapter = new ServeSitesAdapter(R.layout.item_serve_site, data);
        mapAroundSpot.setAdapter(adapter);
        mapAroundSpot.addItemDecoration(CommonUtil.getRecyclerDivider(this));
        ArrayList<MarkerOptions> list = new ArrayList<>();
        for (ServeSites serveSites : data) {
            markerOption = new MarkerOptions().draggable(false);
            markerOption.icon(ICON_RED);
            list.add(markerOption);
        }
        ArrayList<Marker> markers = mAMap.addMarkers(list, true);
        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            ServeSites serveSite = data.get(i);
            marker.setPosition(new LatLng(serveSite.lat, serveSite.lng));
            marker.setObject(serveSite);
        }
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            serveSite = data.get(position);
            showSiteInfo(serveSite);
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(data.get(position).lat, serveSite.lng)));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        });
    }
}
