package com.wuyou.user.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.widget.LoadingDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyou.user.Constant;
import com.wuyou.user.bean.UserInfo;


/**
 * Created by hjn on 2016/12/29.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e(TAG, "onReq: " + baseReq.toString());
        if (baseReq.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(TAG, "onReq: " + baseResp.toString());
        if (baseResp instanceof SendAuth.Resp) {
            doThirdLogin(baseResp);
        }
    }

    private void doThirdLogin(final BaseResp baseResp) {
    }

    private void getUserInfo(String assigned) {
    }


    private void saveUserInfo(BaseResponse<UserInfo> response) {
    }

}
