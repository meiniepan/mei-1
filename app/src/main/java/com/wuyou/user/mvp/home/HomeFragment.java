package com.wuyou.user.mvp.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.wuyou.user.bean.ActivityBean;
import com.wuyou.user.bean.CommunityBean;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.ShareBean;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.bean.response.CommunityListResponse;
import com.wuyou.user.bean.response.HomeVideoResponse;
import com.wuyou.user.bean.response.ListResponse;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.JZVideoPlayerFullscreen;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HomeMapActivity;
import com.wuyou.user.view.activity.SearchActivity;
import com.wuyou.user.view.activity.WebActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.MarqueeTextView;
import com.wuyou.user.view.widget.panel.ShareBottomBoard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class HomeFragment extends BaseFragment implements JZVideoPlayerFullscreen.OnShareListener {
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
    @BindView(R.id.home_address)
    TextView homeAddress;
    @BindView(R.id.home_order_message)
    MarqueeTextView homeOrderMessage;
    @BindView(R.id.home_activity)
    ImageView homeActivity;

    @BindView(R.id.home_order_area)
    View homeOrderArea;
    @BindView(R.id.home_refresh)
    SwipeRefreshLayout homeRefresh;

    private String communityId = "0";
    private CommunityBean cacheCommunityBean;
    private List<CommunityBean> communityBeans;
    private List<HomeVideoBean> videoData;
    private HomeVideoBean homeVideoBean1;
    private HomeVideoBean homeVideoBean2;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mCtx);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        mainServeList.setLayoutManager(mLinearLayoutManager);
        mainServeList.setHasFixedSize(true);
        mainServeList.setNestedScrollingEnabled(false);

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        setCacheData();
        initVideo();
        initLocationAndGetData();
        getOrderMessage();
        getActivityData();
        homeRefresh.setOnRefreshListener(() -> {
            getOrderMessage();
            if (location == null && mLocationClient != null) mLocationClient.startLocation();
        });
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
            homeCurrentLocation.setText(cacheCommunityBean.name + "社区，附近有 45 家服务商");
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
        location.setAoiName(poiItem.getTitle());

        String currentCommunityId = getCurrentCommunityId(communityBeans);
        if (!TextUtils.equals(currentCommunityId, communityId)) {
            Log.e("Test", "onAddressChanged: 社区更改！！！！！！！！！！");
            getCommunityData(currentCommunityId);
        } else {
            Log.e("Test", "onAddressChanged: 社区没变！！！！！！！！！！");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        getOrderMessage();
        if (CarefreeDaoSession.getInstance().getUserInfo() == null)
            mLocationClient.startLocation(); //退出登录，重新定位社区
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
                HomeFragment.this.dismissDialog();
                ToastUtils.ToastMessage(mCtx, getString(R.string.location_error));
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
                        if (homeRefresh.isRefreshing()) homeRefresh.setRefreshing(false);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (homeRefresh.isRefreshing()) homeRefresh.setRefreshing(false);
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

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, getString(R.string.get_community_fail));
                    }
                });
    }

    public void getCommunityData(String currentCommunityId) {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getVideos(currentCommunityId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .doOnNext(response -> getServeList()) //获取服务列表
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
                handler.post(() -> setCommunityText(currentCommunity));
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
                handler.post(() -> showLocationChangedAlert(currentCommunity, cacheCommunityBean));
            } else {
                Log.e("Test", "getCurrentCommunityId: 有缓存，当前社区和缓存社区一样的！！！！！");
                handler.post(() -> setCommunityText(currentCommunity));
            }
        }
        return communityId;
    }

    Handler handler = new Handler();

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
        homeAddress.post(() -> setCommunityText(currentCommunity));
    }

    private void setCommunityText(CommunityBean currentCommunity) {
        if (currentCommunity == null) {
            homeAddress.setText(location.getAoiName());
            homeCurrentLocation.setText("该地址附近有 0 家服务商");
        } else {
            homeAddress.setText(location.getAoiName() + currentCommunity.name);
            homeCurrentLocation.setText(cacheCommunityBean.name + "社区，附近有 45 家服务商");
        }

    }

    //查找当前位置所在的社区
    private CommunityBean findCurrentCommunity(List<CommunityBean> list) {
        if (list == null) return null;
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng data;
        for (CommunityBean bean : list) {
            data = new LatLng(bean.lat, bean.lng);
            float lineDistance = AMapUtils.calculateLineDistance(current, data);
            if (lineDistance < 2000) {    //如果当前位置和社区中心点坐标小于2000米，视为在社区里
                return bean;
            }
        }
        return null;
    }

    public void setVideoData(List<HomeVideoBean> videoData) {
        this.videoData = videoData;
        if (videoData != null && this.videoData.size() > 1) {
            homeVideoBean1 = videoData.get(0);
            video1.setUp(homeVideoBean1.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean1.title);
            homeVideoTitle1.setText(homeVideoBean1.title);
            GlideUtils.loadImage(mCtx, homeVideoBean1.preview, video1.thumbImageView);
            video1.addShareListener(this);

            homeVideoBean2 = videoData.get(1);
            video2.setUp(homeVideoBean2.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean2.title);
            homeVideoTitle2.setText(homeVideoBean2.title);
            GlideUtils.loadImage(mCtx, homeVideoBean2.preview, video2.thumbImageView);
        } else {
            ToastUtils.ToastMessage(mCtx, "获取视频信息失败" + videoData);
        }
    }

    @OnClick({R.id.home_location_area, R.id.home_map, R.id.home_search, R.id.home_activity})
    public void onViewClicked(View view) {
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
            case R.id.home_search:
                intent.setClass(mCtx, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_activity:
                if (TextUtils.isEmpty(Constant.WEB_URL)) return;
                intent.setClass(mCtx, WebActivity.class);
                if (CarefreeDaoSession.getInstance().getUserInfo() == null) {
                    intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL);
                } else {
                    intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL + "?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken());
                }
                startActivity(intent);
                break;
        }
    }

    public void getOrderMessage() {
        if (CarefreeDaoSession.getInstance().getUserInfo() == null) {
            homeRefresh.setRefreshing(false);
            return;
        }
        CarefreeRetrofit.getInstance().createApi(OrderApis.class).getOrderList(QueryMapBuilder.getIns().put("user_id", CarefreeDaoSession.getInstance().getUserId()).put("status", "2").put("startId", "0").put("flag", "1").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderListResponse> orderListResponseBaseResponse) {
                        setOrderData(orderListResponseBaseResponse.data.list);
                        homeRefresh.setRefreshing(false);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        homeRefresh.setRefreshing(false);
                    }
                });
    }

    public void setOrderData(List<OrderBean> orderData) {
        if (orderData != null && orderData.size() > 0) {
            homeOrderArea.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            for (OrderBean orderBean : orderData) {
                stringBuilder.append("  您有一条新订单，订单编号: ").append(orderBean.order_number);
            }
            homeOrderMessage.setText(stringBuilder.toString());
        }
    }


    @Override
    public void onShare(String url, int platform) {
        ShareBottomBoard bottomBoard = new ShareBottomBoard(mCtx);
        if (TextUtils.equals(url, homeVideoBean1.video)) {
            bottomBoard.setData(copyToShare(homeVideoBean1));
        } else {
            bottomBoard.setData(copyToShare(homeVideoBean2));
        }
        bottomBoard.show();
        bottomBoard.setOnDismissListener(dialog -> getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
    }

    private ShareBean copyToShare(HomeVideoBean homeVideoBean) {
        ShareBean shareBean = new ShareBean();
        shareBean.targetUrl = homeVideoBean.video;
        shareBean.miniPath = ""; //TODO
        shareBean.miniType = 0;
        shareBean.preview = homeVideoBean.preview;
        shareBean.summary = homeVideoBean.summary;
        shareBean.title = homeVideoBean.title;
        return shareBean;
    }

    public void getActivityData() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getActivityData(QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ActivityBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ActivityBean>> listResponseBaseResponse) {
                        setActivityData(listResponseBaseResponse.data.list);
                    }
                });
    }

    private ActivityBean activityBean;

    public void setActivityData(List<ActivityBean> activityData) {
        if (activityData != null && activityData.size() > 0) {
            activityBean = activityData.get(0);
            GlideUtils.loadImage(mCtx, activityBean.image, homeActivity);
            Constant.WEB_URL = activityBean.link;
        }
    }
}
