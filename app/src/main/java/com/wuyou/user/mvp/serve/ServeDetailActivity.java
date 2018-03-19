package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.mvp.store.StoreDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.glide.GlideUtils;
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
    @BindView(R.id.serve_detail_title)
    TextView serveDetailTitle;
    @BindView(R.id.serve_detail_count)
    TextView serveDetailCount;
    @BindView(R.id.serve_detail_price)
    TextView serveDetailPrice;
    @BindView(R.id.serve_detail_description)
    WebView serveDetailDescription;
    @BindView(R.id.serve_detail_store)
    TextView serveDetailStore;
    @BindView(R.id.create_order_serve_point)
    TextView createOrderServePoint;
    @BindView(R.id.create_order_serve_comment_count)
    TextView createOrderServeCommentCount;
    @BindView(R.id.serve_detail_comment_star)
    ProperRatingBar serveDetailCommentStar;
    @BindView(R.id.serve_detail_comment_star_count)
    TextView serveDetailCommentStarCount;
    @BindView(R.id.serve_detail_comment_content)
    TextView serveDetailCommentContent;
    @BindView(R.id.serve_detail_known)
    ListView serveDetailKnown;
    private ServeDetailBean serviceDetail;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        showLoadingDialog();
        String id = getIntent().getStringExtra(Constant.SERVE_ID);
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeDetail(id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeDetailBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeDetailBean> serveDetailBeanBaseResponse) {
                        setData(serveDetailBeanBaseResponse.data);
                    }
                });

        findViewById(R.id.serve_detail_comment_area).setOnClickListener(v -> {
            //jump to comment
        });
        findViewById(R.id.serve_detail_store_area).setOnClickListener(v -> {
            if (serviceDetail == null) return;
            Intent intent = new Intent(getCtx(), StoreDetailActivity.class);
            intent.putExtra(Constant.STORE_ID, serviceDetail.shop_id);
            startActivity(intent);
        });


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_detail;
    }


    public void buyNow(View view) {
        if (!checkUser(this)) return;
        Intent intent = new Intent(getCtx(), NewOrderActivity.class);
        intent.putExtra(Constant.SERVE_BEAN, serviceDetail);
        startActivity(intent);
    }

    public void setData(ServeDetailBean serviceDetail) {
        this.serviceDetail = serviceDetail;
        GlideUtils.loadImageNoHolder(this, serviceDetail.photo, serveDetailPicture);
        serveDetailTitle.setText(serviceDetail.title);
        serveDetailCount.setText(serviceDetail.recorded + "");
        serveDetailPrice.setText(serviceDetail.price + "");
        serveDetailDescription.loadData(serviceDetail.content, "text/html", "UTF-8");
        serveDetailStore.setText(serviceDetail.shop_name);
        createOrderServePoint.setText(serviceDetail.high_praise);
        serveDetailCommentStar.setRating(serviceDetail.star);
        serveDetailCommentStarCount.setText(serviceDetail.star + "");
    }
}
