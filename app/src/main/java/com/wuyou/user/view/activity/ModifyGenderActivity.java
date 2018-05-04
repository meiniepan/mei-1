package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/3/14.
 */

public class ModifyGenderActivity extends BaseActivity {
    @BindView(R.id.cb_1)
    CheckBox cbMale;
    @BindView(R.id.cb_2)
    CheckBox cbFemale;
    @BindView(R.id.cb_3)
    CheckBox cbSecret;
    private String sGender;

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_gender;
    }


    @OnClick({R.id.btn_modify_nick,R.id.cb_1,R.id.cb_2,R.id.cb_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_modify_nick:
                setResult(RESULT_OK,new Intent().putExtra("info",sGender));
                finish();
                break;
            case R.id.cb_1:
                initCheck();
                cbMale.setChecked(true);
                sGender = "男";
                break;
            case R.id.cb_2:
                initCheck();
                cbFemale.setChecked(true);
                sGender = "女";
                break;
            case R.id.cb_3:
                initCheck();
                cbSecret.setChecked(true);
                sGender = "保密";
                break;
        }
    }

    private void initCheck() {
        cbMale.setChecked(false);
        cbFemale.setChecked(false);
        cbSecret.setChecked(false);
    }
}
