package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.OrderBean;
import com.wuyou.user.event.OrderEvent;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.OrderApis;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by DELL on 2018/3/14.
 */

public class CommentActivity extends BaseActivity {
    @BindView(R.id.comment_store)
    TextView comment;
    @BindView(R.id.comment_star)
    ProperRatingBar commentStar;
    @BindView(R.id.comment_anonymous)
    CheckBox anonymousButton;
    private OrderBean orderBean;
    private String serveId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderBean = getIntent().getParcelableExtra(Constant.ORDER_BEAN);
        comment.setText(orderBean.shop.shop_name);
        serveId = getIntent().getStringExtra(Constant.SERVE_ID);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment;
    }

    public void submitComment(View view) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .createComment(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().put("order_id", orderBean.order_id).put("service_id", serveId)
                        .put("star", commentStar.getRating() * 2 + "").put("anonymous", anonymousButton.isChecked() ? "1" : "0").buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "评价成功");
                        EventBus.getDefault().post(new OrderEvent());
                        Intent intent = new Intent(getCtx(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
