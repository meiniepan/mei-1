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
import com.wuyou.user.R;
import com.wuyou.user.bean.HomeVideoBean;
import com.wuyou.user.util.WechatShareModel;

import butterknife.ButterKnife;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created by hjn on 2016/11/17.
 */
public class ShareBottomBoard extends Dialog implements View.OnClickListener {
    Context mCtx;
    private WechatShareModel shareModel;
    private HomeVideoBean homeVideoBean;

    public ShareBottomBoard(Context context) {
        super(context, R.style.my_dialog);
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
        if (homeVideoBean == null) return;
        switch (v.getId()) {
            case R.id.share_board_wx:
                ShareUtil.shareMedia(mCtx, SharePlatform.WX, homeVideoBean.title, "", homeVideoBean.video, homeVideoBean.preview, new CustomShareListener());
                break;
            case R.id.share_board_moment:
                ShareUtil.shareMedia(mCtx, SharePlatform.WX_TIMELINE, homeVideoBean.title, "", homeVideoBean.video, homeVideoBean.preview, new CustomShareListener());
                break;
            case R.id.share_cancel:
                dismiss();
                break;
        }
    }

    public void setData(HomeVideoBean data) {
        this.homeVideoBean = data;
    }

    class CustomShareListener extends ShareListener {
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
