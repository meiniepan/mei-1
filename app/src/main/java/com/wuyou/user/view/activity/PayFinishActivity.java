package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.AppManager;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.SimpleResponse;
import com.wuyou.user.mvp.order.OrderDetailActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.util.RxUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/5/7.
 */

public class PayFinishActivity extends BaseActivity {
    @BindView(R.id.pay_finish_title)
    TextView payFinishTitle;
    @BindView(R.id.pay_finish_top)
    TextView payFinishTop;
    private String targetId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        targetId = getIntent().getStringExtra(Constant.ORDER_ID);
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getPayStatus(targetId, QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<SimpleResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<SimpleResponse> response) {
                        if (response.data.is_paid != 1) {
                            payFinishTitle.setText("支付未完成，请到订单详情查看");
                            payFinishTop.setText("支付未完成");
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }
                });
    }

    private void back() {
        Intent intent = new Intent();
        intent.setClass(getCtx(), MainActivity.class);
        intent.putExtra(Constant.MAIN_FLAG, 1);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_result;
    }

    @OnClick({R.id.pay_finish_go_detail, R.id.pay_finish_back_main})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.pay_finish_go_detail:
                intent.setClass(getCtx(), OrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ID, targetId);
                startActivity(intent);
                finish();
                AppManager.getAppManager().finishActivity(SearchActivity.class);
                break;
            case R.id.pay_finish_back_main:
                intent.setClass(getCtx(), MainActivity.class);
                intent.putExtra(Constant.MAIN_FLAG, 1);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
