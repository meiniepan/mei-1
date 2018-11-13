package com.wuyou.user.mvp.kyc;

import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.data.api.AuthTokenResponse;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/19.
 */

public class KycAuthActivity extends BaseActivity {
    @BindView(R.id.tv_kyc_name)
    TextView tvKycName;
    @BindView(R.id.tv_kyc_id)
    TextView tvKycId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_kyc_auth;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("KYC认证");
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getAuthInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<AuthTokenResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AuthTokenResponse> authTokenResponseBaseResponse) {
                        tvKycName.setText(authTokenResponseBaseResponse.data.name);
                        tvKycId.setText(authTokenResponseBaseResponse.data.id_card_number);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }
                });

    }
}
