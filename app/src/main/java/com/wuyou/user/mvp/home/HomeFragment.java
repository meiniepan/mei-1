package com.wuyou.user.mvp.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.google.gson.GsonBuilder;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.CommunityBean;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.bean.response.CommunityListResponse;
import com.wuyou.user.bean.response.HomeVideoResponse;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.mvp.address.AddressActivity;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.mvp.order.OrderListAdapter;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.layoutmanager.FullLinearLayoutManager;
import com.wuyou.user.util.JZVideoPlayerFullscreen;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HomeMapActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static cn.jzvd.JZVideoPlayer.FULLSCREEN_ORIENTATION;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.main_video_1)
    JZVideoPlayerFullscreen video1;
    @BindView(R.id.main_video_2)
    JZVideoPlayerFullscreen video2;
    @BindView(R.id.main_serve_list)
    RecyclerView mainServeList;
    @BindView(R.id.home_video_title_1)
    TextView homeVideoTitle1;
    @BindView(R.id.home_video_title_2)
    TextView homeVideoTitle2;
    @BindView(R.id.home_current_location)
    TextView homeCurrentLocation;
    @BindView(R.id.home_order_list)
    RecyclerView homeOrderList;
    @BindView(R.id.home_address)
    TextView homeAddress;


    private String communityId = "0";
    private CommunityBean cacheCommunityBean;
    private List<CommunityBean> communityBeans;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mainServeList.setLayoutManager(new FullLinearLayoutManager(mCtx));
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        setCacheData();
        initVideo();
        initLocationAndGetData();
        getOrderList();
    }

    private void setCacheData() {
        String categoryCache = SharePreferenceManager.getInstance(mCtx).getStringValue(Constant.CATEGORY_CACHE);
        if (!TextUtils.isEmpty(categoryCache)) {
            CategoryListResponse categoryListResponse = new GsonBuilder().create().fromJson(categoryCache, CategoryListResponse.class);
            setData(categoryListResponse.list);
        }
        String gson = SharePreferenceManager.getInstance(mCtx).getStringValue(Constant.CACHE_COMMUNITY);
        if (!TextUtils.isEmpty(gson)) {
            cacheCommunityBean = new GsonBuilder().create().fromJson(gson, CommunityBean.class);
            communityId = cacheCommunityBean.community_id;
            homeAddress.setText(cacheCommunityBean.address + cacheCommunityBean.name);
        }
    }

    private void initVideo() {
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressChanged(AddressEvent event) {
        PoiItem poiItem = event.poiItem;
        location.setLatitude(poiItem.getLatLonPoint().getLatitude());
        location.setLongitude(poiItem.getLatLonPoint().getLongitude());
        location.setStreet(poiItem.getTitle());

        String currentCommunityId = getCurrentCommunityId(communityBeans);
        if (!TextUtils.equals(currentCommunityId, communityId)) {
            Log.e("Test", "onAddressChanged: 社区更改！！！！！！！！！！");
            getCommunityData(currentCommunityId);
        } else {
            Log.e("Test", "onAddressChanged: 社区没变！！！！！！！！！！");
        }
    }


    private AMapLocationClient mLocationClient = null;
    private AMapLocation location;

    private void initLocationAndGetData() {
        showLoadingDialog();
        mLocationClient = new AMapLocationClient(mCtx);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                location = aMapLocation;
                getCommunityList();
            } else {
                dismissDialog();
                ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
            }
        });
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private void getServeList() {
        Log.e("Test", "getServeList: 获取服务信息");
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getCategoryList(communityId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CategoryListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CategoryListResponse> orderListResponseBaseResponse) {
                        String dataToJson = new GsonBuilder().create().toJson(orderListResponseBaseResponse.data);
                        SharePreferenceManager.getInstance(mCtx).setValue(Constant.CATEGORY_CACHE, dataToJson);
                        setData(orderListResponseBaseResponse.data.list);
                    }
                });
    }

    public void setData(List<CategoryParent> data) {
        for (int i = 0; i < data.size(); i++) {
            for (CategoryChild categoryChild : data.get(i).sub) {
                categoryChild.position = i;
            }
        }
        mainServeList.setAdapter(new MainServeAdapter(R.layout.item_main_serve, data, mCtx));
    }

    public void getCommunityList() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class)
                .getCommunitiesList(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<BaseResponse<CommunityListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CommunityListResponse> communityListResponseBaseResponse) {
                        communityBeans = communityListResponseBaseResponse.data.list;
                        communityId = getCurrentCommunityId(communityBeans);
                        getCommunityData(communityId);
                    }
                });
    }

    public void getCommunityData(String currentCommunityId) {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getVideos(currentCommunityId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .doOnNext(response -> getServeList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeVideoResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<HomeVideoResponse> homeVideoResponseBaseResponse) {
                        setVideoData(homeVideoResponseBaseResponse.data.list);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, "获取首页信息失败");
                    }
                });

    }

    private String getCurrentCommunityId(List<CommunityBean> list) {
        CommunityBean currentCommunity = findCurrentCommunity(list);
        if (null == currentCommunity) { // 当前位置没有社区服务点
            Log.e("Test", "getCurrentCommunityId: 当前位置没有社区服务点!!!!!!");
            homeAddress.post(this::showNoMatchAlert);
            if (null == cacheCommunityBean) {
                homeAddress.post(() -> homeAddress.setText(location.getStreet() + currentCommunity.name));
            }
            return communityId;
        }
        //有社区服务点之后逻辑
        if (cacheCommunityBean == null) {       //没有缓存，直接存
            Log.e("Test", "getCurrentCommunityId: 没有缓存，直接展示社区服务点信息");
            saveNewCommunity(currentCommunity);
        } else {                                //有缓存，比较之后再存
            if (!TextUtils.equals(currentCommunity.community_id, cacheCommunityBean.community_id)) { //当前社区和缓存社区不同
                Log.e("Test", "getCurrentCommunityId: 有缓存，当前社区和缓存社区不同");
                homeAddress.post(() -> showLocationChangedAlert(currentCommunity, cacheCommunityBean));
            } else {
                Log.e("Test", "getCurrentCommunityId: 有缓存，当前社区和缓存社区一样的！！！！！");
                homeAddress.post(() -> homeAddress.setText(location.getStreet() + currentCommunity.name));
            }
        }
        return communityId;
    }

    private void showNoMatchAlert() {
        new CustomAlertDialog.Builder(mCtx).setTitle("提示").setMessage("检测到您当前位置没有服务点").setPositiveButton("知道了", null).create().show();
    }

    private void showLocationChangedAlert(CommunityBean currentCommunity, CommunityBean cacheCommunityBean) {
        new CustomAlertDialog.Builder(mCtx).setTitle("提示").setMessage("检查到您之前的位置是" + cacheCommunityBean.address + "，是否更换为" + currentCommunity.address)
                .setPositiveButton("更换", (dialog, which) -> saveNewCommunity(currentCommunity))
                .create().show();
    }

    private void saveNewCommunity(CommunityBean currentCommunity) {
        currentCommunity.address = location.getStreet();
        SharePreferenceManager.getInstance(mCtx).setValue(Constant.CACHE_COMMUNITY, new GsonBuilder().create().toJson(currentCommunity));
        communityId = currentCommunity.community_id;
        cacheCommunityBean = currentCommunity;
        homeAddress.post(() -> homeAddress.setText(location.getStreet() + currentCommunity.name));
    }

    //查找当前位置所在的社区
    private CommunityBean findCurrentCommunity(List<CommunityBean> list) {
        if (list == null) return null;
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng data;
        for (CommunityBean bean : list) {
            data = new LatLng(bean.lat, bean.lng);
            float lineDistance = AMapUtils.calculateLineDistance(current, data);
            if (lineDistance < 15571323) {    //如果当前位置和社区中心点坐标小于2000米，视为在社区里
                return bean;
            }
        }
        return null;
    }

    public void setVideoData(List<HomeVideoBean> videoData) {
        if (videoData != null && videoData.size() > 1) {
            HomeVideoBean homeVideoBean1 = videoData.get(0);
            video1.setUp(homeVideoBean1.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean1.title);
            homeVideoTitle1.setText(homeVideoBean1.title);
            GlideUtils.loadRoundCornerImage(mCtx, homeVideoBean1.preview, video1.thumbImageView, 4);
            HomeVideoBean homeVideoBean2 = videoData.get(1);
            video2.setUp(homeVideoBean2.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean2.title);
            homeVideoTitle2.setText(homeVideoBean2.title);
            GlideUtils.loadRoundCornerImage(mCtx, homeVideoBean2.preview, video2.thumbImageView, 4);
        } else {
            ToastUtils.ToastMessage(mCtx, "获取视频信息失败" + videoData);
        }
    }

    public void getOrderList() {
        if (CarefreeDaoSession.getInstance().getUserInfo() == null) return;
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderList(CarefreeDaoSession.getInstance().getUserId(), 2, 0 + "", 1, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {
                        setOrderData(orderListResponseBaseResponse.data.list);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, R.string.get_order_fail);
                    }
                });
    }

    public void setOrderData(List<OrderBean> orderData) {
        homeOrderList.setLayoutManager(new FullLinearLayoutManager(mCtx));
        OrderListAdapter adapter = new OrderListAdapter(mCtx,R.layout.item_order_list, orderData);
        adapter.setButtonGone();
        homeOrderList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            OrderBean bean = (OrderBean) adapter1.getData().get(position);
            Intent intent = new Intent(mCtx, OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, bean.id);
            startActivity(intent);
        });
    }

    @OnClick({R.id.home_location_area, R.id.home_map})
    public void onViewClicked(View view) {
        if (location == null) return;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.home_location_area:
                intent.setClass(mCtx, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.home_map:
                intent.setClass(mCtx, HomeMapActivity.class);
                startActivity(intent);
                break;
        }
    }


}
