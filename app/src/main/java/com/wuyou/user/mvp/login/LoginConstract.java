package com.wuyou.user.mvp.login;

import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface LoginConstract {
    interface View extends IBaseView {
        void loginSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void doLogin();

        abstract void getUserInfo();
    }
}
