package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wuyou.user.R;
import com.wuyou.user.util.layoutmanager.AutoLineFeedLayoutManager;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/6/21.
 */

public class HomePictureDialog extends Dialog {
    public HomePictureDialog(@NonNull Context context) {
        super(context, R.style.zoom_dialog);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.picture_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
