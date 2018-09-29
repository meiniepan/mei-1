package com.wuyou.user.mvp.block;

import android.support.v4.util.ArraySet;

import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import java.util.ArrayList;

/**
 * Created by DELL on 2018/9/26.
 */

public interface BlockMainContract {

    interface View extends IBaseView {
        void getBlockHeightSuccess(String height);
        void getTransactionsAmountSuccess(String amount);
        void getAccountAmountSuccess(String amount);
        void getPointTypeAmountSuccess(String amount);
        void getOriginDataSuccess(ArraySet amount);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getBlockHeight();
        abstract void getTransactionsAmount();
        abstract void getAccountAmount();
        abstract void getPointTypeAmount();
        abstract void getOriginData();
    }
}
