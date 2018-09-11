package com.wuyou.user.mvp.wallet;

import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by DELL on 2018/9/3.
 */

public interface WalletContract {

    interface View extends IBaseView {
        void signUpSuccess();

        void getWalletInfoSuccess();

        void createAccountSuccess();

        void checkCaptchaSuccess();
    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void signUp();

        public abstract void createAccount(String name, String phone);

        abstract void getWalletInfo();

        abstract void getPointRecord();

        abstract void getCaptcha(String type);

        abstract void checkCaptcha(String type, String phone, String captcha);
    }
}
