package com.wuyou.user.mvp.login;

import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface LoginContract {
    interface View extends IBaseView {
        void loginSuccess();
        void getVerifySuccess();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void doLogin(String phone,String captcha);
        abstract void getVerifyCode(String phone);
    }
}
