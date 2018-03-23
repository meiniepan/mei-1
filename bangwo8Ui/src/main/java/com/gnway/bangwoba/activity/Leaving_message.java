package com.gnway.bangwoba.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gnway.bangwoba.R;
import com.gnway.bangwoba.global.Variable;

public class Leaving_message extends AppCompatActivity implements View.OnClickListener{
    private WebView mwebView;
    private String url="http://www.bangwo8.com/osp2016/chat/chatMessage_phone.php?client="+ Variable.loginUser+"&vendorID="+Variable.AgentId+"&from=sdk";
    private View goback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaving_message);
        initView();
    }
    protected void initView() {
        goback=findViewById(R.id.goback);
        goback.setOnClickListener(this);
        mwebView=(WebView)findViewById(R.id.robot_webview);

        settingWebView();
        mwebView.loadUrl(url);
    }
    @SuppressLint("NewApi")
    private void settingWebView() {
        try {
            //支持获取手势焦点，输入用户名、密码或其他
            mwebView.requestFocusFromTouch();
            mwebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mwebView.setScrollbarFadingEnabled(false);
            final WebSettings settings = mwebView.getSettings();
            settings.setJavaScriptEnabled(true);  //支持js
            mwebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mwebView.removeJavascriptInterface("accessibilityTraversal");
            mwebView.removeJavascriptInterface("accessibility");
            //  设置自适应屏幕，两者合用
            settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            settings.supportMultipleWindows();  //多窗口
            settings.setAllowFileAccess(true);  //设置可以访问文件
            settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
            settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            settings.setSavePassword(false);
            //设置编码格式
            settings.setDefaultTextEncodingName("UTF-8");
            // 关于是否缩放
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                settings.setDisplayZoomControls(false);
            }
            /**
             *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
             *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            }
            settings.setLoadsImagesAutomatically(true);  //支持自动加载图片

            settings.setDomStorageEnabled(true); //开启DOM Storage

            mwebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    finish();
                }
            });

            mwebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.goback){
            finish();
        }
    }
}
