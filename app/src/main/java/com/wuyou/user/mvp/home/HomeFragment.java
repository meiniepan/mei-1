package com.wuyou.user.mvp.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.wuyou.user.data.remote.ActivityBean;
import com.wuyou.user.data.remote.CommunityBean;
import com.wuyou.user.data.remote.HomeVideoBean;
import com.wuyou.user.data.remote.ShareBean;
import com.wuyou.user.data.remote.response.CategoryChild;
import com.wuyou.user.data.remote.response.CategoryListResponse;
import com.wuyou.user.data.remote.response.CategoryParent;
import com.wuyou.user.data.remote.response.CommunityListResponse;
import com.wuyou.user.data.remote.response.ListResponse;
import com.wuyou.user.event.AddressEvent;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.mvp.address.AddressActivity;
import com.wuyou.user.mvp.score.ScoreExchangeActivity;
import com.wuyou.user.mvp.score.ScoreMissionActivity;
import com.wuyou.user.mvp.wallet.ScoreAccountActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ActivityApis;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideBannerLoader;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HomeMapActivity;
import com.wuyou.user.view.activity.SearchActivity;
import com.wuyou.user.view.activity.WebActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.JZVideoPlayerFullscreen;
import com.wuyou.user.view.widget.panel.ShareBottomBoard;
import com.wuyou.user.view.widget.pullToResfresh.HomeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

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

public class HomeFragment extends BaseFragment implements JZVideoPlayerFullscreen.OnShareListener {
    @BindView(R.id.main_serve_list)
    RecyclerView mainServeList;
    @BindView(R.id.home_address)
    TextView homeAddress;
    @BindView(R.id.home_activity)
    Banner homeActivityBanner;

    @BindView(R.id.home_refresh)
    HomeRefreshLayout refreshLayout;
    @BindView(R.id.home_scroll_view)
    NestedScrollView homeScrollView;
    @BindView(R.id.home_video)
    JZVideoPlayerFullscreen video;

    private String communityId = "0";
    private CommunityBean cacheCommunityBean;
    private List<CommunityBean> communityBeans;
    private MainServeAdapter adapter;
    private HomeVideoBean homeVideoBean;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initServeList();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        setCacheData();
        initVideo();
        if (askForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initLocationAndGetData();
        }
        initBanner();
        getActivityData();
        getServeList(); //先取社区ID为0 的数据 填充界面
        getVideo();
        refreshLayout.setOnRefreshListener(() -> {
            getActivityData();
            if (askForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getServeList();
            }
        });
    }

    private void initBanner() {
        homeActivityBanner.setImageLoader(new GlideBannerLoader(true));
        homeActivityBanner.setOffscreenPageLimit(4);
        homeActivityBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        homeActivityBanner.setIndicatorGravity(BannerConfig.CENTER);
        homeActivityBanner.setDelayTime(3000);
        homeActivityBanner.isAutoPlay(true);
//        homeActivityBanner.setPageTransformer(true, new GalleryTransformer());
    }

    private void initServeList() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mCtx);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        mainServeList.setLayoutManager(mLinearLayoutManager);
        mainServeList.setHasFixedSize(true);
        mainServeList.setNestedScrollingEnabled(false);
        adapter = new MainServeAdapter(R.layout.item_main_serve);
        mainServeList.setAdapter(adapter);
    }

    @Override
    protected void permissionGranted() {
        refreshLayout.completeRefresh();
        if (mLocationClient == null) {
            initLocationAndGetData();
        }
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
        location.setAoiName(poiItem.getTitle());

        String currentCommunityId = getCurrentCommunityId(communityBeans);
        if (!TextUtils.equals(currentCommunityId, communityId)) {
            Log.e("Carefree", "onAddressChanged:  社区更改！！！！！！！！！！");
        } else {
            Log.e("Carefree", "onAddressChanged: 社区没变！！！！！！！！！！");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
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
        Log.e("Carefree", "getServeList: 获取服务信息");
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getCategoryList(communityId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .doOnNext(categoryListResponseBaseResponse -> {
                    String dataToJson = new GsonBuilder().create().toJson(categoryListResponseBaseResponse.data);
                    SharePreferenceManager.getInstance(mCtx).setValue(Constant.CATEGORY_CACHE, dataToJson);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CategoryListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CategoryListResponse> orderListResponseBaseResponse) {
                        setData(orderListResponseBaseResponse.data.list);
                        refreshLayout.completeRefresh();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        refreshLayout.completeRefresh();
                    }
                });
    }

    public void setData(List<CategoryParent> data) {
        for (int i = 0; i < data.size(); i++) {
            for (CategoryChild categoryChild : data.get(i).sub) {
                categoryChild.position = i;
            }
        }
        adapter.setNewData(data);
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
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, getString(R.string.get_community_fail));
                    }
                });
    }

    public void getVideo() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class).getVideos(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeVideoBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<HomeVideoBean> homeVideoResponseBaseResponse) {
                        setVideoData(homeVideoResponseBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, "获取首页信息失败");
                    }
                });
    }

    public void setVideoData(HomeVideoBean videoData) {
        homeVideoBean = videoData;
        video.setUp(homeVideoBean.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean.title);
        GlideUtils.loadImage(getContext(), homeVideoBean.preview, video.thumbImageView);
        video.addShareListener(this);
        video.setCustomData(homeVideoBean);
    }

    private String getCurrentCommunityId(List<CommunityBean> list) {
        CommunityBean currentCommunity = findCurrentCommunity(list);
        if (null == currentCommunity) { // 当前位置没有社区服务点
            Log.e("Carefree", "getCurrentCommunityId: 当前位置没有社区服务点!!!!!!");
            homeAddress.post(this::showNoMatchAlert);
            if (null == cacheCommunityBean) {
                handler.post(() -> setCommunityText(currentCommunity));
            }
            return communityId;
        }
        //有社区服务点之后逻辑
        if (cacheCommunityBean == null) {       //没有缓存，直接存
            Log.e("Carefree", "getCurrentCommunityId: 没有缓存，直接展示社区服务点信息");
            saveNewCommunity(currentCommunity);
        } else {                                //有缓存，比较之后再存
            if (!TextUtils.equals(currentCommunity.community_id, cacheCommunityBean.community_id)) { //当前社区和缓存社区不同
                Log.e("Carefree", "getCurrentCommunityId: 有缓存，当前社区和缓存社区不同");
                handler.post(() -> showLocationChangedAlert(currentCommunity, cacheCommunityBean));
            } else {
                Log.e("Carefree", "getCurrentCommunityId: 有缓存，当前社区和缓存社区一样的！！！！！");
                handler.post(() -> setCommunityText(currentCommunity));
            }
        }
        return communityId;
    }

    Handler handler = new Handler();

    private void showNoMatchAlert() {
//        new CustomAlertDialog.Builder(mCtx).setTitle("提示").setMessage("检测到您当前位置没有服务点").setPositiveButton("知道了", null).create().show();
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
        } else {
            homeAddress.setText(location.getAoiName() + currentCommunity.name);
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

    @OnClick({R.id.home_location_area, R.id.home_map, R.id.home_search, R.id.home_score_account, R.id.home_score_mission, R.id.home_score_exchange})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.home_location_area:
                intent.setClass(mCtx, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.home_map:
                if (CommonUtil.checkNetworkNoConnected(mCtx)) return;
                intent.setClass(mCtx, HomeMapActivity.class);
                startActivity(intent);
                break;
            case R.id.home_search:
                intent.setClass(mCtx, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_score_account:
                if (!checkUser(getContext())) return;
                intent.setClass(mCtx, ScoreAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.home_score_mission:
                if (!checkUser(getContext())) return;
                intent.setClass(mCtx, ScoreMissionActivity.class);
                startActivity(intent);
                break;
            case R.id.home_score_exchange:
                intent.setClass(mCtx, ScoreExchangeActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void getActivityData() {
        CarefreeRetrofit.getInstance().createApi(ActivityApis.class).getActivityData(QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<ActivityBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<ActivityBean>> listResponseBaseResponse) {
                        setActivityData(listResponseBaseResponse.data.list);
                    }
                });
    }

    public void setActivityData(List<ActivityBean> activityData) {
        if (activityData != null && activityData.size() > 0) {
            ArrayList<String> images = new ArrayList<>();
            for (ActivityBean activityBean : activityData) {
                images.add(activityBean.image);
            }
            homeActivityBanner.setOnBannerListener(position -> startWebActivity(activityData.get(position).link));
            homeActivityBanner.setImages(images);
            homeActivityBanner.start();
        }
    }

    private void startWebActivity(String activityUrl) {
        Intent intent = new Intent();
        if (CommonUtil.checkNetworkNoConnected(mCtx)) return;
        if (TextUtils.isEmpty(activityUrl)) return;
        intent.setClass(mCtx, WebActivity.class);
        if (CarefreeDaoSession.getInstance().getUserInfo() == null) {
            intent.putExtra(Constant.WEB_INTENT, activityUrl);
        } else {
            Uri uri = Uri.parse(activityUrl);
            if (TextUtils.isEmpty(uri.getQuery())) {
                intent.putExtra(Constant.WEB_INTENT, activityUrl + "?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken());
            } else {
                intent.putExtra(Constant.WEB_INTENT, activityUrl + "&user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken());
            }
        }
        startActivity(intent);
    }

    @Override
    public void onShare(String url, int platform) {
        if (homeVideoBean == null) return;
        ShareBottomBoard bottomBoard = new ShareBottomBoard(getContext());
        if (TextUtils.equals(url, homeVideoBean.video)) {
            bottomBoard.setData(copyToShare(homeVideoBean));
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
}
