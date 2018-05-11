package com.wuyou.user.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.JSBean;

import butterknife.BindView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by hjn on 2017/2/28.
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_title)
    TextView tvTitle;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.common_dark);
        setUpWebView();
        String url = getIntent().getStringExtra(Constant.WEB_URL);
        int type = getIntent().getIntExtra(Constant.WEB_TYPE, 0);
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
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
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
                Log.e("Test", "onPageFinished: " + Thread.currentThread());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Test", "shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                return true;
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
                tvTitle.setText(title);
            }
        });


        //js调用本地方法
        webView.addJavascriptInterface(new JSCallJavaInterface(), "root");
    }


    private String loadJSMethod(String methodName) {
        final String[] result = new String[1];
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webView.evaluateJavascript("javascript:" + methodName, value -> result[0] = value);
        } else {
            webView.loadUrl("javascript:" + methodName);
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
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.clearHistory();

        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }


    private class JSCallJavaInterface {
        @JavascriptInterface
        public void hybridProtocol(String json) {
//        ToastUtils.ToastMessage(CarefreeApplication.getInstance().getApplicationContext(), message);
            Log.e("Test", "ShareActivity: " + Thread.currentThread());
            Log.e("Test", "ShareActivity: " + new Gson().fromJson(json, JSBean.class).activityid);
        }
    }
}
