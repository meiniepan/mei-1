package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.RxUtil;

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
            case Constant.EMAIL:
                findViewById(R.id.info_update_mark).setVisibility(View.GONE);
                tvTitle.setText("修改邮箱");
                etNick.setHint("请填写邮箱");
                etNick.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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
                if (etNick.length() == 0) return;
                switch (from) {
                    case Constant.NICK:
                        updateInfo("nickname", etNick.getText().toString());
                        break;
                    case Constant.EMAIL:
                        updateInfo("email", etNick.getText().toString());
                        break;
                }

                break;
        }
    }


    private void updateInfo(String key, String value) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                        .put("field", key)
                        .put("value", value)
                        .buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        setResult(RESULT_OK, new Intent().putExtra("info", etNick.getText().toString()));
                        finish();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });

    }
}
