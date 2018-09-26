package com.wuyou.user.mvp.block;

import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import org.bson.Document;

/**
 * Created by DELL on 2018/9/26.
 */

public interface BlockContract {

    public interface View extends IBaseView {
        void getBlockInfoSuccess(BlockInfo blockInfo);

        void getTransactionSuccess(Document transactionInfo);

        void getAccountInfoSuccess(EosAccountInfo accountInfo);


    }

    public abstract class Presenter extends BasePresenter<View> {
        abstract void getBlockInfo(String searchText);
        abstract void getTransactionInfo(String searchText);
        abstract void getAccountInfo(String searchText);

    }
}
