package com.wuyou.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.JSBean;
import com.wuyou.user.bean.NativeToJsBean;
import com.wuyou.user.bean.ShareBean;
import com.wuyou.user.bean.response.WxPayResponse;
import com.wuyou.user.event.WXPayEvent;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.MoneyApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.widget.panel.ShareBottomBoard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.shaohui.shareutil.share.ShareListener;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by hjn on 2017/2/28.
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_title)
    TextView tvTitle;
    private JSBean jsBean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setUpWebView();
        String url = getIntent().getStringExtra(Constant.WEB_INTENT);
        int type = getIntent().getIntExtra(Constant.WEB_TYPE, 0);
        if (type == 1) {
            findViewById(R.id.web_top).setVisibility(View.VISIBLE);
        }
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    private void setUpWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(false);
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        String deviceInfo = Utils.getDeviceInfo(BaseApplication.getInstance().getApplicationContext()) + " app/Android";
        webSettings.setUserAgentString(deviceInfo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        webView.requestFocusFromTouch();
//        webView.requestFocus();
//        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("Test", "onPageStarted: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Test", "shouldOverrideUrlLoading: " + url);
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                Log.e("Test", "onReceivedError: " + errorCode + "..." + description);
                switch (errorCode) {
                    case 404:
                    case 500:
                        break;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                tvTitle.setText(title);
            }
        });


        //js调用本地方法
        webView.addJavascriptInterface(new JSCallNativeInterface(), "root");
    }


    private String loadJSMethod(String methodName, String json) {
        final String[] result = new String[1];
        String js = "javascript:" + methodName + "(" + json + ")";
        Log.e("Test", "loadJSMethod: " + js);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webView.evaluateJavascript(js, value -> {
                result[0] = value;
            });
        } else {
            webView.loadUrl(js);
        }
        return result[0];
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_web;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.clearHistory();

        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    private class JSCallNativeInterface {
        @JavascriptInterface
        public void hybridProtocol(String json) {
            jsBean = new Gson().fromJson(json, JSBean.class);
            Log.e("Test", "JSCallNativeInterface: " + jsBean.toString());
            Intent intent = new Intent();
            if (TextUtils.equals(jsBean.MethodName, "UserLogin")) {
                intent.setClass(getCtx(), LoginActivity.class);
                startActivityForResult(intent, 201);
            } else if (TextUtils.equals(jsBean.MethodName, "AppGoBack")) {
                finish();
            } else if (TextUtils.equals(jsBean.MethodName, "GoApplyPage")) {
                payInWx(jsBean.OrderId);
            } else if (TextUtils.equals(jsBean.MethodName, "ShareActivity")) {
                webView.post(() -> doShare(jsBean.CallBackName, jsBean.ActivityUrl, jsBean.ActivityTitle));
            } else if (TextUtils.equals(jsBean.MethodName, "SaveQCode")) {
                saveQRCode();
            }
        }
    }

    private void saveQRCode() {
        if (ContextCompat.checkSelfPermission(getCtx(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
            if (CommonUtil.createQRImage(jsBean.ImgUrl)) {
                ToastUtils.ToastMessage(getCtx(), R.string.save_success);
            } else {
                ToastUtils.ToastMessage(getCtx(), R.string.save_fail);
            }
        }
    }

    private IWXAPI msgApi;

    private void payInWx(String order_id) {
        msgApi = WXAPIFactory.createWXAPI(CarefreeApplication.getInstance().getApplicationContext(), null);
        msgApi.registerApp(Constant.WX_ID);
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getActivityWXPayOrderInfo(order_id, QueryMapBuilder.getIns()
                .put("uid", CarefreeDaoSession.getInstance().getUserId())
                .put("pay_type", "APP")
                .put("is_mini_program", "0").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WxPayResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<WxPayResponse> baseResponseBaseResponse) {
                        doPay(baseResponseBaseResponse.data);
                    }
                });
    }

    private void doPay(WxPayResponse data) {
        PayReq request = new PayReq();
        request.appId = data.appid;
        request.partnerId = data.mch_id;
        request.prepayId = data.prepay_id;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.nonce_str;
        request.timeStamp = data.timestamp;
        request.sign = data.sign;
        msgApi.sendReq(request);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXPayFinish(WXPayEvent event) {
        NativeToJsBean bean = new NativeToJsBean();
        if (event.errCode == 0) {
            bean.ApplyStatus = "1";
        } else {
            bean.ApplyStatus = "2";
        }
        webView.post(() -> loadJSMethod(jsBean.CallBackName, new Gson().toJson(bean)));
    }

    private void doShare(String callback, String activityUrl, String activityTitle) {
        NativeToJsBean nativeToJsBean = new NativeToJsBean();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ShareBottomBoard bottomBoard = new ShareBottomBoard(this);
        ShareBean bean = new ShareBean();
        bean.previewBitmap = bmp;
        bean.miniPath = "pages/index/index";
        bean.miniType = 0;
        bean.summary = getString(R.string.share_activity);
        bean.targetUrl = activityUrl;
        bean.title = activityTitle;
        bottomBoard.setData(bean);
        bottomBoard.addShareListener(new ShareListener() {
            @Override
            public void shareSuccess() {
                nativeToJsBean.ShareStatus = "1";
                loadJSMethod(callback, new Gson().toJson(nativeToJsBean));
            }

            @Override
            public void shareFailure(Exception e) {
                nativeToJsBean.ShareStatus = "2";
                loadJSMethod(callback, new Gson().toJson(nativeToJsBean));
            }

            @Override
            public void shareCancel() {
            }
        });
        bottomBoard.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 201) {
            NativeToJsBean bean = new NativeToJsBean();
            bean.Authorization = CarefreeDaoSession.getInstance().getUserInfo().getToken();
            bean.UserId = CarefreeDaoSession.getInstance().getUserId();
            webView.post(() -> loadJSMethod(jsBean.CallBackName, new Gson().toJson(bean)));
        }
    }

}