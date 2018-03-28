package com.wuyou.user.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

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


    @OnClick({R.id.info_account_area, R.id.info_email_area, R.id.info_sex_area, R.id.info_birthday_area, R.id.info_edit, R.id.info_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.info_head:
            case R.id.info_edit:
            case R.id.info_account_area:
            case R.id.info_email_area:
            case R.id.info_sex_area:
            case R.id.info_birthday_area:
                ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                break;
        }
    }
}
