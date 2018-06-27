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

import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryChild;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/6/21.
 */

public class HomePictureDialog extends Dialog {
    private String categoryId;

    public HomePictureDialog(@NonNull Context context, CategoryChild item) {
        super(context, R.style.zoom_dialog);
        categoryId = item.id;
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

        ImageView emptyImage = rootView.findViewById(R.id.home_empty_picture);
        setEmptyPicture(emptyImage);

    }

    public void setEmptyPicture(ImageView emptyPicture) {
        switch (categoryId) {
            case "38":
                emptyPicture.setImageResource(R.mipmap.home_market_empty);
                break;
            case "39":
                emptyPicture.setImageResource(R.mipmap.home_education_empty);
                break;
            case "53":
                emptyPicture.setImageResource(R.mipmap.home_education_ask_empty);
                break;
            case "46":
                emptyPicture.setImageResource(R.mipmap.home_help_empty);
                break;
            case "35":
                emptyPicture.setImageResource(R.mipmap.home_health_empty);
                break;
            case "52":
                emptyPicture.setImageResource(R.mipmap.home_no_serve_dialog_bg);
                break;
            default:
                emptyPicture.setImageResource(R.mipmap.home_no_serve_dialog_bg);
                break;
        }
    }
}
