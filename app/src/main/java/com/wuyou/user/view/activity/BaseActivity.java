package com.wuyou.user.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.SystemBarTintManager;
import com.gs.buluo.common.widget.LoadingDialog;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;
import com.wuyou.user.mvp.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {
    View mRoot;
    protected P mPresenter;
    protected Toolbar mToolbar;
    private int color = R.color.login_press;
    private Unbinder bind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        AppManager.getAppManager().addActivity(this);
        setExplode();//new Slide()  new Fade()
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
        mRoot = createView();
        mRoot.setBackgroundColor(getResources().getColor(R.color.white));
        setContentView(mRoot);
        mToolbar = findViewById(getToolBarId());

//        setSupportActionBar(mToolbar);
        bindView(savedInstanceState);
        initSystemBar(this);
        View backView = findViewById(R.id.back);
        if (backView != null) {
            backView.setOnClickListener(view -> finish());
        }
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

    private View createView() {
        View view = LayoutInflater.from(this).inflate(getContentLayout(), null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onDestroy() {
        if (bind != null) {
            bind.unbind();
        }
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

    protected void showLoadingDialog(boolean cancel) {
        LoadingDialog.getInstance().show(mRoot.getContext(), R.string.loading, cancel);
    }

    protected void dismissDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }


    protected abstract void bindView(Bundle savedInstanceState);

    protected abstract int getContentLayout();


    protected boolean checkUser(Context context) {
        if (CarefreeApplication.getInstance().getUserInfo() == null) {
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

    public int getToolBarId() {
        return 0;
    }
}


