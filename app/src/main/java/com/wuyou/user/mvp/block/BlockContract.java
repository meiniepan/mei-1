package com.wuyou.user.mvp.block;

import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.data.remote.TransactionBean;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import org.bson.Document;

import java.util.ArrayList;

/**
 * Created by DELL on 2018/9/26.
 */

public interface BlockContract {

    interface View extends IBaseView {
        void getBlockInfoSuccess(BlockInfo blockInfo);

        void getTransactionSuccess(Document transactionInfo);

        void getAccountInfoSuccess(EosAccountInfo accountInfo);


        void getAccountTransactionsSuccess(ArrayList<TransactionBean> transactionBeans);

        void getAccountTransactionsMore(ArrayList<TransactionBean> document);

        void getBlockTransactionSuccess(ArrayList<TransactionBean> transactionBeans);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getBlockInfo(String searchText);

        abstract void getTransactionInfo(String searchText);

        abstract void getAccountInfo(String searchText);

        abstract void getAccountTransfers(String accountName);

        abstract void getAccountTransferMore(String account_name);

        abstract void getBlockTransfer(int blockNumber);
    }
}
