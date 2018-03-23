package com.gnway.bangwoba.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.gnway.bangwoba.R;

/**
 * Created by luzhan on 2017/7/6.
 */

public class PhotoLookActivity extends AppCompatActivity {

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        initStateBar();
        String imagePath = getIntent().getStringExtra("path");
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        bitmap = BitmapFactory.decodeFile(imagePath);
        photoView.setImageBitmap(bitmap);
        ViewCompat.setTransitionName(photoView, "image");
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android 5.0之后就可以使用了，单击图片后以共享元素的形式返回上一个activity
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });
    }
    private void initStateBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.exit_in,R.anim.exit_out);
    }
}
