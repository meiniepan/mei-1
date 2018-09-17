package com.wuyou.user.mvp.wallet;

import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by DELL on 2018/9/3.
 */

public interface WalletContract {

    interface View extends IBaseView {
        void createAccountSuccess();

        void checkCaptchaSuccess();
    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void createAccount(String name, String phone);

        abstract void getWalletInfo();

        abstract void getCaptcha(String type, String phone);

        abstract void checkCaptcha(String type, String phone, String captcha);
    }
}
