package com.wuyou.user.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.SystemBarTintManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.tendcloud.tenddata.TCAgent;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;
import com.wuyou.user.mvp.login.LoginActivity;
import com.wuyou.user.util.QMUIStatusBarHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {
    View mRoot;
    protected P mPresenter;
    private int color = R.color.white;
    private TextView titleIconView;
    private View titleTextLayout;
    private TextView titleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
        super.onCreate(savedInstanceState);
        init();
        AppManager.getAppManager().addActivity(this);
        setExplode();//new Slide()  new Fade()
        initContentView(R.layout.layout_base_activity);
        bindView(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initSystemBar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, getLocalClassName());
    }

    private void initContentView(int layout_base_activity) {
        setContentView(layout_base_activity);
        findViewById(R.id.back_base).setOnClickListener(v -> onBackPressed());
        titleIconView = findViewById(R.id.iv_title_icon);
        titleTextView = findViewById(R.id.base_tv_title);
        titleTextLayout = findViewById(R.id.base_title_layout);
        baseStatusLayout = findViewById(R.id.id_status);
        createView();
    }

    protected void getStatusData() {
        baseStatusLayout.showProgressView();
    }

    protected void enableErrorAction(){
        baseStatusLayout.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatusData();
            }
        });
    }

    protected void disableFitSystemWindow() {
        findViewById(R.id.base_root).setFitsSystemWindows(false);
    }

    protected void setTitleVisiable(int type) {
        titleTextLayout.setVisibility(type);
    }

    protected void setTitleIcon(int resId, View.OnClickListener listener) {
        titleIconView.setVisibility(View.VISIBLE);
        titleIconView.setBackgroundResource(resId);
        titleIconView.setOnClickListener(listener);
    }

    protected void setTitleIconText(int resId, View.OnClickListener listener) {
        titleIconView.setVisibility(View.VISIBLE);
        titleIconView.setText(resId);
        titleIconView.setOnClickListener(listener);
    }

    protected void setTitleText(String title) {
        titleTextLayout.setVisibility(View.VISIBLE);
        titleTextView.setText(title);
    }

    protected void setTitleText(int titleId) {
        titleTextLayout.setVisibility(View.VISIBLE);
        titleTextView.setText(titleId);
    }

    protected void setBackVisiable(int type) {
        findViewById(R.id.back).setVisibility(type);
    }

    public StatusLayout baseStatusLayout;

    protected void showErrMessage(String message) {
        baseStatusLayout.showErrorView(message);
    }
    protected void  showEmpty(String message){
        baseStatusLayout.showEmptyView(message);
    }

    protected void showErrMessage(int msgId) {
        baseStatusLayout.showErrorView(getString(msgId));
    }

    protected void showLoadingView() {
        baseStatusLayout.showProgressView();
    }

    protected void showLoadingView(String message) {
        baseStatusLayout.showProgressView(message);
    }

    protected void showContent() {
        baseStatusLayout.showContentView();
    }

    private void createView() {
        ViewStub viewStub = findViewById(R.id.id_stub);
        viewStub.setLayoutResource(getContentLayout());
        mRoot = viewStub.inflate();
        View back = mRoot.findViewById(R.id.back);
        if (back != null)
            back.setOnClickListener(v -> onBackPressed());
        ButterKnife.bind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setExplode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void init() {

    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        LoadingDialog.getInstance().dismissDialog();
        super.onStop();
    }

    public View getRootView() {
        return mRoot;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorInt
     */
    public void setBarColor(int colorInt) {
        color = colorInt;
        initSystemBar(this);
    }

    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showLoadingDialog() {
        LoadingDialog.getInstance().show(mRoot.getContext(), R.string.loading, true);
    }

    protected void showLoadingDialog(String message) {
        LoadingDialog.getInstance().show(mRoot.getContext(), message, true);
    }

    protected void showLoadingDialog(boolean cancel) {
        LoadingDialog.getInstance().show(mRoot.getContext(), R.string.loading, cancel);
    }

    protected void dismissDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }


    protected abstract int getContentLayout();

    protected abstract void bindView(Bundle savedInstanceState);


    protected boolean checkUser(Context context) {
        if (CarefreeDaoSession.getInstance().getUserId() == null) {
            ToastUtils.ToastMessage(context, R.string.please_login);
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    protected P getPresenter() {
        return null;
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
    }

    //    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onUpdate(UpdateEvent event) {
//        UpdatePanel updatePanel = new UpdatePanel(AppManager.getAppManager().currentActivity(), event);
//        updatePanel.setCancelable(event.supported);
//        updatePanel.show();
//        Beta.checkUpgrade(false, false);
//    }

    protected Context getCtx() {
        return this;
    }

    protected boolean askForPermissions(String... permissions) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission(this, permission);
            if (selfPermission == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else if (selfPermission == PackageManager.PERMISSION_DENIED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[]{}), 1);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            permissionDenied();
            ToastUtils.ToastMessage(this, getString(R.string.permession_denied));
        }
    }

    protected void permissionDenied() {

    }

    protected void permissionGranted() {
    }
}


