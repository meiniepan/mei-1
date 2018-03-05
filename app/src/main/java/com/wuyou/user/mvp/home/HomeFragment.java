package com.wuyou.user.mvp.home;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryListResponse;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.JZVideoPlayerFullscreen;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
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

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        video1.backButton.setVisibility(View.GONE);
        video1.batteryLevel.setVisibility(View.GONE);
        video1.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "嫂子辛苦了");
        GlideUtils.loadRoundCornerImage(mCtx, "https://i0.hdslb.com/bfs/archive/646d2bfc4e1a323e4be028c5469cd4d874ecf9d5.jpg", video1.thumbImageView, 4);

        video2.backButton.setVisibility(View.GONE);
        video2.batteryLevel.setVisibility(View.GONE);
        video2.setUp("http://aliqncdn.miaopai.com/stream/awPst5XIMQH2~Vg5cVLCzkuRXUyW8eMoIXlBPg___32.mp4?ssig=3371dbe5d8831fda9e94e8f301902f22&time_stamp=1520230718294&f=/awPst5XIMQH2~Vg5cVLCzkuRXUyW8eMoIXlBPg___32.mp4", JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子快长大");
        GlideUtils.loadRoundCornerImage(mCtx, "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640", video2.thumbImageView, 4);

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

    @Override
    public void showError(String message, int res) {

    }

    public void setData(List<CategoryParent> data) {
        mainServeList.setLayoutManager(new LinearLayoutManager(mCtx));
        for (int i = 0; i < data.size(); i++) {
            for (CategoryChild categoryChild : data.get(i).sub) {
                categoryChild.position = i;
            }
        }
        mainServeList.setAdapter(new MainServeAdapter(R.layout.item_main_serve, data, mCtx));
    }
}
