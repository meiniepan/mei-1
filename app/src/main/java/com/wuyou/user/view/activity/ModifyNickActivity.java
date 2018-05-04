package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/3/14.
 */

public class ModifyNickActivity extends BaseActivity {
    @BindView(R.id.et_input_nick)
    EditText etNick;
    @BindView(R.id.tv_info_title)
    TextView tvTitle;
    private String from;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        from = getIntent().getStringExtra(Constant.FROM);
        switch (from) {
            case Constant.NICK:
                tvTitle.setText("修改昵称");
                etNick.setHint("请填写昵称");
                break;
            case Constant.PHONE:
                tvTitle.setText("修改手机号");
                etNick.setHint("请填写手机号");
                break;
            case Constant.EMAIL:
                tvTitle.setText("修改邮箱");
                etNick.setHint("请填写邮箱");
                break;

        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_nick;
    }


    @OnClick({R.id.btn_modify_nick})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_modify_nick:
                setResult(RESULT_OK,new Intent().putExtra("info",etNick.getText().toString()));
                finish();
                break;
        }
    }
}
