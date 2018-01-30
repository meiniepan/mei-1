package com.wuyou.user.mvp.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_verify)
    EditText loginVerify;

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.login_verify, R.id.login, R.id.login_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_verify:
                Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
                ToastUtils.ToastMessage(getCtx(), CarefreeApplication.getInstance().getUserInfo().getUserName());
                break;
            case R.id.login:
                UserInfo userInfo = new UserInfo();
                userInfo.setId(1000);
                userInfo.setUserName("哈哈哈");
                userInfo.setPhone("13131313131");
                CarefreeApplication.getInstance().getUserInfoDao().insert(userInfo);
                break;
            case R.id.login_protocol:
                ToastUtils.ToastMessage(getCtx(), CarefreeApplication.getInstance().getUserInfo().getUserName());
                break;
        }
    }
}
