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
import android.widget.ImageView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.wuyou.user.R;
import com.wuyou.user.util.glide.GlideUtils;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/6/21.
 */

public class HomePictureDialog extends Dialog {
    private String imageUrl;

    public HomePictureDialog(@NonNull Context context, String imageUrl) {
        super(context, R.style.zoom_dialog);
        this.imageUrl = imageUrl;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.picture_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = DensityUtils.dip2px(getContext(), 315);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        ImageView emptyImage = rootView.findViewById(R.id.home_empty_picture);
        GlideUtils.loadImageWithListener(getContext(), imageUrl, emptyImage, () -> LoadingDialog.getInstance().dismissDialog());
        rootView.setOnClickListener(v -> dismiss());
    }
}
