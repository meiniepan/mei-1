package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.SignInBean;
import com.wuyou.user.util.RxUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by DELL on 2018/11/27.
 */

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.scan_zxing)
    ZXingView mZXingView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mZXingView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e("Carefree", "onScanQRCodeSuccess: " + result);
        if (result.contains("signIn://")) {
            signIn(result.split("signIn://")[1]);
        } else {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.wrong_qr_code));
            mZXingView.startSpotDelay(500); // 延迟0.1秒后开始识别
        }
        vibrate();
    }

    private void signIn(String data) {
        showLoadingDialog();
        SignInBean bean = new Gson().fromJson(data, SignInBean.class);
        EoscDataManager.getIns().registerTimeBank(bean.id + "", bean.organizer, bean.name)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        ToastUtils.ToastMessage(getCtx(), R.string.sign_success);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        mZXingView.startSpotDelay(500); // 延迟0.1秒后开始识别
                        if (message.message.contains("you have registered")) {
                            ToastUtils.ToastMessage(getCtx(), "您已经签到了");
                        } else {
                            ToastUtils.ToastMessage(getCtx(), message.message);
                        }
                    }
                });
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        if (isDark) {
            ToastUtils.ToastMessage(getCtx(), "环境过暗,打开闪光灯");
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.ToastMessage(getCtx(), "启动扫码失败");
    }


    @OnClick({R.id.iv_qr_pic, R.id.iv_qr_flash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_qr_pic:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .isZoomAnim(true)
                        .sizeMultiplier(0.5f)
                        .setOutputCameraPath("/CustomPath")
                        .withAspectRatio(1, 1)
                        .openClickSound(false)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.iv_qr_flash:
                mZXingView.openFlashlight();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia localMedia = selectList.get(0);
                    String path = localMedia.getPath();
                    mZXingView.decodeQRCode(path);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        mZXingView.closeFlashlight();
        mZXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy();
        super.onDestroy();
    }
}
