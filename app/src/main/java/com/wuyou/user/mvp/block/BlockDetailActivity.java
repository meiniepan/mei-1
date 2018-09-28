package com.wuyou.user.mvp.block;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import org.bson.Document;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockDetailActivity extends BaseActivity<BlockContract.View, BlockContract.Presenter> implements BlockContract.View {
    @BindView(R.id.block_detail_account_view)
    ViewStub blockDetailAccountView;
    @BindView(R.id.block_detail_block_view)
    ViewStub blockDetailBlockView;
    @BindView(R.id.block_detail_transaction)
    ViewStub blockDetailTransaction;

    private String searchText;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        searchText = getIntent().getStringExtra(Constant.SEARCH_TEXT);
        showLoadingView(getString(R.string.searching));
        doSearch();
    }

    @Override
    protected BlockContract.Presenter getPresenter() {
        return new BlockPresenter();
    }

    private void doSearch() {
        setTitleText(R.string.searching);
        if (searchText.length() == 0) return;
        Pattern pNum = Pattern.compile("[0-9]*");
        if (searchText.length() == 12 && searchText.charAt(0) >= 'a' && searchText.charAt(0) <= 'z') { //account
            mPresenter.getAccountInfo(searchText);
        } else if (pNum.matcher(searchText).matches()) {//number  区块高度
            mPresenter.getBlockInfo(searchText);
        } else {
            mPresenter.getBlockInfo(searchText);
            mPresenter.getTransactionInfo(searchText);

        }
    }

    private boolean hasView = false; //防止两个并发请求前后返回时， 无数据遮盖有数据，

    @Override
    protected int getContentLayout() {
        return R.layout.activity_block_account_detail;
    }

    @Override
    public void getBlockInfoSuccess(BlockInfo blockInfo) {
        setTitleText(R.string.block_info);
        showContent();
        hasView = true;
        BlockView blockInfoView = new BlockView(blockDetailBlockView.inflate());
    }

    @Override
    public void getTransactionSuccess(Document transactionInfo) {
        setTitleText(R.string.transaction_detail);
        showContent();
        hasView = true;
        TransactionView blockTransferView = new TransactionView(blockDetailTransaction.inflate());
        blockTransferView.blockTransferJson.setText(CommonUtil.stringToJSON(transactionInfo.toJson()));
    }

    @Override
    public void getAccountInfoSuccess(EosAccountInfo accountInfo) {
        setTitleText(R.string.account_detail);
        showContent();
        AccountView blockAccountView = new AccountView(blockDetailAccountView.inflate());
        blockAccountView.blockAccountName.setText(accountInfo.account_name);
        blockAccountView.blockAccountCreateTime.setText(accountInfo.created);
    }


    class AccountView {
        @BindView(R.id.block_account_name)
        TextView blockAccountName;
        @BindView(R.id.block_account_creator)
        TextView blockAccountCreator;
        @BindView(R.id.block_account_create_time)
        TextView blockAccountCreateTime;
        @BindView(R.id.block_account_transactions)
        RecyclerView blockAccountTransactions;

        AccountView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class TransactionView {
        @BindView(R.id.block_transfer_json)
        TextView blockTransferJson;

        TransactionView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class BlockView {
        BlockView(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void showError(String message, int res) {
        if (!hasView) showEmpty(message);
    }

}
