package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.bean.PointBean;
import com.wuyou.user.bean.ServeSites;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.network.apis.ScoreApis;
import com.wuyou.user.util.NetTool;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/6/6.
 */

public class SignInFragment extends BaseFragment {
    @BindView(R.id.sign_in_title)
    TextView signInTitle;
    @BindView(R.id.sign_in_desc)
    TextView signInDesc;
    @BindView(R.id.sign_in)
    TextView signIn;
    @BindView(R.id.sign_arrange_spot)
    TextView signArrangeSpot;
    @BindView(R.id.sign_re_location)
    TextView signReLocation;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        NetTool.openGPS(mCtx);
        initLocationAndGetData();
        mRootView.findViewById(R.id.sign_in).setOnClickListener(v -> signUp());
        mRootView.findViewById(R.id.sign_re_location).setOnClickListener(v -> {
            signReLocation.setText("定位中...");
            mLocationClient.startLocation();
        });
    }

    private AMapLocationClient mLocationClient = null;

    private void initLocationAndGetData() {
        showLoadingDialog();
        mLocationClient = new AMapLocationClient(mCtx);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                centerLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                getServeSites();
                Log.e("Carefree", "initLocationAndGetData: " + aMapLocation.getLocationType() + "........" + aMapLocation.getAccuracy());
            } else {
                dismissDialog();
                signReLocation.setText("重新定位");
                ToastUtils.ToastMessage(mCtx, getString(R.string.location_error));
            }
        });
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationOption.setNeedAddress(false);
        mLocationClient.startLocation();
    }

    private void signUp() {
        if (System.currentTimeMillis() - CarefreeApplication.getInstance().lastSignTime >= 60 * 1000) {
            showLoadingDialog();
            CarefreeRetrofit.getInstance().createApi(ScoreApis.class)
                    .signIn(QueryMapBuilder.getIns().put("uid", CarefreeDaoSession.getInstance().getUserId()).buildPost())
                    .compose(RxUtil.switchSchedulers())
                    .subscribe(new BaseSubscriber<BaseResponse<PointBean>>() {
                        @Override
                        public void onSuccess(BaseResponse<PointBean> baseResponse) {
                            ToastUtils.ToastMessage(mCtx, R.string.sign_success);
                            CarefreeApplication.getInstance().lastSignTime = System.currentTimeMillis();
                        }
                    });
        } else {
            ToastUtils.ToastMessage(mCtx, "您签到太频繁，请稍后再试");
        }
    }

    private LatLng centerLatLng;

    public void getServeSites() {
        CarefreeRetrofit.getInstance().createApi(HomeApis.class)
                .getServeSites(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .map(listResponseBaseResponse -> {
                    List<ServeSites> list = listResponseBaseResponse.data.list;
                    if (list == null || list.size() == 0) return new ServeSites();
                    ServeSites nearSite = list.get(0);
                    float minDistance = AMapUtils.calculateLineDistance(new LatLng(nearSite.lat, nearSite.lng), centerLatLng);
                    for (ServeSites serveSites : list) {
                        float distance = AMapUtils.calculateLineDistance(new LatLng(serveSites.lat, serveSites.lng), centerLatLng);
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearSite = serveSites;
                        }
                    }
                    if (minDistance < 100) {
                        return nearSite;
                    } else {
                        return new ServeSites();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ServeSites>() {
                    @Override
                    public void onSuccess(ServeSites serveSites) {
                        setSiteInfo(serveSites);
                    }
                });
    }

    public void setSiteInfo(ServeSites siteInfo) {
        if (isDetached()) return;
        if (siteInfo.name == null) {
            signArrangeSpot.setText("对不起，您不在签到范围内 \n 如果定位不准确，请打开wifi开关重新定位");
            signReLocation.setText("重新定位");
            signIn.setEnabled(false);
        } else {
            signInTitle.setText(siteInfo.name);
            signInDesc.setText(siteInfo.introduce);
            signInDesc.setVisibility(View.VISIBLE);
            signArrangeSpot.setText("您已进入站点签到范围：" + siteInfo.name);
            signIn.setEnabled(true);
            signReLocation.setText("重新定位");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) mLocationClient.stopLocation();
    }
}
