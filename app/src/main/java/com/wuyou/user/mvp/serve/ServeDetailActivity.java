package com.wuyou.user.mvp.serve;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.bean.response.ServeDetailResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by hjn on 2018/2/7.
 */

public class ServeDetailActivity extends BaseActivity {
    @BindView(R.id.serve_detail_picture)
    ImageView serveDetailPicture;
    @BindView(R.id.serve_detail_count)
    TextView serveDetailCount;
    @BindView(R.id.serve_detail_serve_time)
    TextView serveDetailServeTime;
    @BindView(R.id.serve_detail_description)
    TextView serveDetailDescription;
    @BindView(R.id.serve_detail_store)
    TextView serveDetailStore;
    @BindView(R.id.create_order_serve_point)
    TextView createOrderServePoint;
    @BindView(R.id.create_order_serve_comment_count)
    TextView createOrderServeCommentCount;
    @BindView(R.id.serve_detail_comment_phone)
    TextView serveDetailCommentPhone;
    @BindView(R.id.serve_detail_comment_star)
    ProperRatingBar serveDetailCommentStar;
    @BindView(R.id.serve_detail_known)
    ListView serveDetailKnown;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        showLoadingDialog();
        String id = getIntent().getStringExtra(Constant.SERVE_ID);
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeDetail(id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeDetailResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeDetailResponse> serveDetailBeanBaseResponse) {
                        setData(serveDetailBeanBaseResponse.data);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_detail;
    }


    public void buyNow(View view) {

    }

    public void setData(ServeDetailResponse data) {
        GlideUtils.loadImageNoHolder(this, data.service_detail.image, serveDetailPicture);
    }
}
