package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.bean.ShareBean;

import butterknife.ButterKnife;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created by hjn on 2016/11/17.
 */
public class ShareBottomBoard extends Dialog implements View.OnClickListener {
    Context mCtx;
    private ShareBean shareMediaBean;

    public ShareBottomBoard(Context context) {
        super(context, R.style.bottom_dialog);
        mCtx = context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.share_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.share_board_wx).setOnClickListener(this);
        rootView.findViewById(R.id.share_board_moment).setOnClickListener(this);
        rootView.findViewById(R.id.share_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (shareMediaBean == null) return;
        switch (v.getId()) {
            case R.id.share_board_wx:
                dismiss();
                if (shareMediaBean.preview != null) {
                    ShareUtil.shareMini(mCtx, SharePlatform.WX, shareMediaBean.title, shareMediaBean.summary, shareMediaBean.targetUrl, shareMediaBean.preview
                            , Constant.WX_MINI_ID, shareMediaBean.miniPath, shareMediaBean.miniType, customShareListener);
                } else {
                    ShareUtil.shareMini(mCtx, SharePlatform.WX, shareMediaBean.title, shareMediaBean.summary, shareMediaBean.targetUrl, shareMediaBean.previewBitmap
                            , Constant.WX_MINI_ID, shareMediaBean.miniPath, shareMediaBean.miniType, customShareListener);
                }
                break;
            case R.id.share_board_moment:
                dismiss();
                if (shareMediaBean.preview != null) {
                    ShareUtil.shareMedia(mCtx, SharePlatform.WX_TIMELINE, shareMediaBean.title, shareMediaBean.summary, shareMediaBean.targetUrl, shareMediaBean.preview, customShareListener);
                } else {
                    ShareUtil.shareMedia(mCtx, SharePlatform.WX_TIMELINE, shareMediaBean.title, shareMediaBean.summary, shareMediaBean.targetUrl, shareMediaBean.previewBitmap, customShareListener);
                }
                break;
            case R.id.share_cancel:
                dismiss();
                break;
        }
    }

    private ShareListener customShareListener = new CustomShareListener();

    public void addShareListener(ShareListener customShareListener) {
        this.customShareListener = customShareListener;
    }

    public void setData(ShareBean data) {
        this.shareMediaBean = data;
    }

    public class CustomShareListener extends ShareListener {
        @Override
        public void shareSuccess() {
            ToastUtils.ToastMessage(mCtx, "分享成功");
        }

        @Override
        public void shareFailure(Exception e) {
            ToastUtils.ToastMessage(mCtx, "分享失败");
        }

        @Override
        public void shareCancel() {
            ToastUtils.ToastMessage(mCtx, "分享取消");
        }
    }
}
