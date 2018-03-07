package com.wuyou.user.mvp.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.bean.OrderBean;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.bean.response.HomeVideoResponse;
import com.wuyou.user.bean.response.OrderListResponse;
import com.wuyou.user.mvp.address.AddressActivity;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.mvp.order.OrderListAdapter;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.HomeApis;
import com.wuyou.user.network.apis.OrderApis;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.FullLinearLayoutManager;
import com.wuyou.user.util.JZVideoPlayerFullscreen;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.HomeMapActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.QueryMap;

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

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getCommunityList();
        getServeList();
        getOrderList();
    }

    private void getServeList() {
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getCategoryList(QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CategoryListResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CategoryListResponse> orderListResponseBaseResponse) {
                        setData(orderListResponseBaseResponse.data.list);
                    }
                });
    }

    public void setData(List<CategoryParent> data) {
        mainServeList.setLayoutManager(new FullLinearLayoutManager(mCtx));
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
                .flatMap(communityListResponseBaseResponse -> CarefreeRetrofit.getInstance().createApi(HomeApis.class).getVideos(communityListResponseBaseResponse.data.list.get(0).community_id,QueryMapBuilder.getIns().buildGet()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeVideoResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<HomeVideoResponse> homeVideoResponseBaseResponse) {
                        setVideoData(homeVideoResponseBaseResponse.data.list);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mCtx, "获取视频信息失败");
                    }
                });
    }

    public void setVideoData(List<HomeVideoBean> videoData) {
        video1.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "标题1");
        GlideUtils.loadRoundCornerImage(mCtx, "https://i0.hdslb.com/bfs/archive/646d2bfc4e1a323e4be028c5469cd4d874ecf9d5.jpg", video1.thumbImageView, 4);
        homeVideoTitle1.setText("我是标题1");

        video2.setUp("http://aliqncdn.miaopai.com/stream/awPst5XIMQH2~Vg5cVLCzkuRXUyW8eMoIXlBPg___32.mp4?ssig=3371dbe5d8831fda9e94e8f301902f22&time_stamp=1520230718294&f=/awPst5XIMQH2~Vg5cVLCzkuRXUyW8eMoIXlBPg___32.mp4", JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "标题2");
        GlideUtils.loadRoundCornerImage(mCtx, "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640", video2.thumbImageView, 4);
        homeVideoTitle2.setText("我是标题2");

//        if (videoData != null && videoData.size() > 1) {
//            HomeVideoBean homeVideoBean1 = videoData.get(0);
//            video1.setUp(homeVideoBean1.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean1.title);
//            homeVideoTitle1.setText(homeVideoBean1.title);
//            GlideUtils.loadRoundCornerImage(mCtx, homeVideoBean1.preview, video1.thumbImageView, 4);
//            HomeVideoBean homeVideoBean2 = videoData.get(1);
//            video2.setUp(homeVideoBean2.video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, homeVideoBean2.title);
//            homeVideoTitle2.setText(homeVideoBean2.title);
//            GlideUtils.loadRoundCornerImage(mCtx, homeVideoBean2.preview, video2.thumbImageView, 4);
//        } else {
//            ToastUtils.ToastMessage(mCtx, "获取视频信息失败" + videoData);
//        }
    }

    public void getOrderList() {
        if (CarefreeApplication.getInstance().getUserInfo() == null) return;
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrderList(CarefreeApplication.getInstance().getUserId(), 2, 0 + "", 1, QueryMapBuilder.getIns().buildGet())
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
        orderData.clear();
        homeOrderList.setLayoutManager(new FullLinearLayoutManager(mCtx));
        OrderListAdapter adapter = new OrderListAdapter(R.layout.item_order_list, orderData);
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
