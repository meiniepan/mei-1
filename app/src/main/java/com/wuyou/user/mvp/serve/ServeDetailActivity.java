package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.ServeDetailBean;
import com.wuyou.user.data.remote.ServeSpecification;
import com.wuyou.user.mvp.store.StoreDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ServeApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideBannerLoader;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.panel.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by hjn on 2018/2/7.
 */

public class ServeDetailActivity extends BaseActivity {
    @BindView(R.id.serve_detail_picture)
    Banner serveDetailBanner;
    @BindView(R.id.serve_detail_title)
    TextView serveDetailTitle;
    @BindView(R.id.serve_detail_count)
    TextView serveDetailCount;
    @BindView(R.id.serve_detail_price)
    TextView serveDetailPrice;
    @BindView(R.id.serve_detail_unit)
    TextView serveDetailUnit;
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
    @BindView(R.id.serve_detail_origin_price)
    TextView serveDetailOriginPrice;
    @BindView(R.id.serve_detail_unit_origin)
    TextView serveDetailUnitOrigin;
    @BindView(R.id.serve_detail_store_area)
    LinearLayout serveDetailStoreArea;
    @BindView(R.id.serve_detail_comment_area)
    LinearLayout serveDetailCommentArea;
    @BindView(R.id.serve_detail_comment_pictures)
    RecyclerView serveDetailCommentPictures;
    @BindView(R.id.serve_detail_on_sale)
    TextView serveDetailOnSale;
    @BindView(R.id.serve_detail_origin_price_layout)
    RelativeLayout serveDetailOriginPriceLayout;
    @BindView(R.id.serve_detail_specification)
    TextView serveDetailSpecification;
    @BindView(R.id.serve_detail_specification_layout)
    LinearLayout serveDetailSpecificationLayout;
    private ServeDetailBean serviceDetail;
    private String id;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.serve_detail);
        serveDetailBanner.setBannerWidth(-1);
        serveDetailBanner.setImageLoader(new GlideBannerLoader());
        serveDetailBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        serveDetailBanner.isAutoPlay(false);
        id = getIntent().getStringExtra(Constant.SERVE_ID);
        findViewById(R.id.serve_detail_store_area).setOnClickListener(v -> {
            if (serviceDetail == null) return;
            Intent intent = new Intent(getCtx(), StoreDetailActivity.class);
            intent.putExtra(Constant.STORE_ID, serviceDetail.shop_id);
            startActivity(intent);
        });

        serveDetailSpecificationLayout.setOnClickListener(v -> buyNow(serveDetailSpecificationLayout));
        getStatusData();
    }

    @Override
    protected void getStatusData() {
        super.getStatusData();
        CarefreeRetrofit.getInstance().createApi(ServeApis.class)
                .getServeDetail(id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServeDetailBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServeDetailBean> serveDetailBeanBaseResponse) {
                        baseStatusLayout.showContentView();
                        setData(serveDetailBeanBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        showErrMessage(e.getDisplayMessage());
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_detail;
    }

    public void buyNow(View view) {
        if (!checkUser(this) || serviceDetail == null) return;
        panel.show();
    }

    public void setData(ServeDetailBean serviceDetail) {
        this.serviceDetail = serviceDetail;
        ArrayList<String> list = new ArrayList<>();
        list.add(serviceDetail.photo);
        serveDetailBanner.setImages((serviceDetail.images == null || serviceDetail.images.size() == 0) ? list : serviceDetail.images);
        serveDetailBanner.start();
        serveDetailTitle.setText(serviceDetail.title);
        serveDetailCount.setText(serviceDetail.sold);
        if (serviceDetail.has_specification == 0) {
            serveDetailPrice.setText(CommonUtil.formatPrice(serviceDetail.price));
            serveDetailSpecificationLayout.setVisibility(View.GONE);
        } else {
            setPriceRange(serviceDetail.specification);
        }
        if (serviceDetail.market_price != 0) {
            serveDetailOriginPriceLayout.setVisibility(View.VISIBLE);
            serveDetailOriginPrice.setText(CommonUtil.formatPrice(serviceDetail.market_price));
        }
        if (!TextUtils.isEmpty(serviceDetail.advert_word)) {
            serveDetailOnSale.setVisibility(View.VISIBLE);
            serveDetailOnSale.setText(serviceDetail.advert_word);
        }
        serveDetailUnit.setText("/" + serviceDetail.unit);
        serveDetailUnitOrigin.setText("/" + serviceDetail.unit);
        serveDetailDescription.loadDataWithBaseURL(null, getNewContent(serviceDetail.content), "text/html", "utf-8", null);
        serveDetailDescription.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        serveDetailStore.setText(serviceDetail.shop_name);
        createOrderServePoint.setText("好评" + serviceDetail.high_praise);
        serveDetailCommentStar.setRating(serviceDetail.star / 2);
        serveDetailCommentStarCount.setText(serviceDetail.star / 2 + "");

        panel = new GoodsChoosePanel(this);
        panel.setData(serviceDetail, serveDetailPrice.getText().toString().trim());
        panel.addShowInDetailListener(goodsSpecification -> {
            serveDetailPrice.setText(CommonUtil.formatPrice(goodsSpecification.price));
            serveDetailSpecification.setText(goodsSpecification.name);
            serveDetailCount.setText(goodsSpecification.sales);
        });
    }

    private GoodsChoosePanel panel;

    public void setPriceRange(List<ServeSpecification> serveSpecifications) {
        float minPrice = serveSpecifications.get(0).price;
        float maxPrice = serveSpecifications.get(0).price;
        for (ServeSpecification serveSpecification : serveSpecifications) {
            if (serveSpecification.price < minPrice) {
                minPrice = serveSpecification.price;
            }
            if (serveSpecification.price > maxPrice) {
                maxPrice = serveSpecification.price;
            }
        }
        if (minPrice == maxPrice || minPrice == 0) {
            serveDetailPrice.setText(CommonUtil.formatPrice(maxPrice));
        } else {
            serveDetailPrice.setText(CommonUtil.formatPrice(minPrice) + "～" + CommonUtil.formatPrice(maxPrice));
        }
    }

    private String getNewContent(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}