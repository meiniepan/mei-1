package com.wuyou.user.mvp.block;

import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.local.LinePoint;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.data.remote.PointBean;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;
import com.wuyou.user.view.widget.lineChart.PointValue;

import org.bson.Document;

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
        void getOriginDataSuccess(ArrayList<LinePoint> amount);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getBlockHeight();
        abstract void getTransactionsAmount();
        abstract void getAccountAmount();
        abstract void getPointTypeAmount();
        abstract void getOriginData();
    }
}
