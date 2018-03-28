package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wuyou.user.R;
import com.wuyou.user.util.WechatShareModel;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/17.
 */
public class ShareBottomBoard extends Dialog implements View.OnClickListener {
    Context mCtx;
    private WechatShareModel shareModel;

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
        switch (v.getId()) {
            case R.id.share_board_wx:
//                ShareUtil.shareMedia(mCtx, SharePlatform.WX);
                break;
            case R.id.share_board_moment:
//                ShareUtil.shareMedia(mCtx, SharePlatform.WX_TIMELINE);
                break;
            case R.id.share_cancel:

                break;
        }
    }

    public void setData(WechatShareModel data) {
        this.shareModel = data;
    }
}
