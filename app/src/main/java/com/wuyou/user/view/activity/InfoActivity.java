package com.wuyou.user.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;

import butterknife.BindView;

/**
 * Created by DELL on 2018/3/14.
 */

public class InfoActivity extends BaseActivity {
    @BindView(R.id.info_head)
    ImageView infoHead;
    @BindView(R.id.info_name)
    TextView infoName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
//        GlideUtils.loadImage(this, userInfo.getHead_image(), infoHead, true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_info;
    }

}
