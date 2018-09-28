package com.wuyou.user.mvp.block;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.remote.BlockInfo;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.view.activity.BaseActivity;

import org.bson.Document;

import java.util.ArrayList;
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
        return new BlockTransferPresenter();
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
        blockInfoView.blockBlockHeight.setText(blockInfo.block_num);
        blockInfoView.blockBlockId.setText(blockInfo.id);
        blockInfoView.blockBlockSpot.setText(blockInfo.producer);
        blockInfoView.blockBlockSpotSign.setText(blockInfo.producer_signature);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getTransactionSuccess(Document transactionInfo) {
        setTitleText(R.string.transaction_detail);
        showContent();
        hasView = true;
        TransactionView blockTransferView = new TransactionView(blockDetailTransaction.inflate());
        blockTransferView.blockTransferId.setText(transactionInfo.getString("trx_id"));
        blockTransferView.blockTransferTime.setText(transactionInfo.get("expiration").toString());
        ArrayList<Document> actions = (ArrayList<Document>) transactionInfo.get("actions");
        blockTransferView.blockTransferActAccount.setText(actions.get(0).getString("account"));
        blockTransferView.blockTransferActAmount.setText(actions.size() + "");
        String transferType = actions.get(0).getString("name");
        blockTransferView.blockTransferType.setText(transferType);

        if ("transfer".equals(transferType)) {
            blockTransferView.blockTransferLayout.setVisibility(View.VISIBLE);
        }
        Document data = (Document) actions.get(0).get("data");
        blockTransferView.blockTransferFrom.setText(data.getString("from"));
        blockTransferView.blockTransferTo.setText(data.getString("to"));
        blockTransferView.blockTransferContent.setText(data.getString("quantity"));
        blockTransferView.blockTransferMemo.setText(data.getString("memo"));

        blockTransferView.blockTransferOriginData.setText(CommonUtil.stringToJSON(transactionInfo.toJson()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getAccountInfoSuccess(EosAccountInfo accountInfo) {
        setTitleText(R.string.account_detail);
        showContent();
        AccountView blockAccountView = new AccountView(blockDetailAccountView.inflate());
        blockAccountView.blockAccountName.setText(accountInfo.account_name);
//        blockAccountView.blockAccountCreator.setText(accountInfo.);
        blockAccountView.blockAccountCreateTime.setText(accountInfo.created);
        String activeThreshold = "";
        String ownerThreashold = "";
        for (EosAccountInfo.PermissionsBean permissionsBean : accountInfo.permissions) {
            if ("actitive".equals(permissionsBean.perm_name)) {
                blockAccountView.blockAccountActiveKey.setText(permissionsBean.required_auth.keys.get(0).key);
                activeThreshold = permissionsBean.required_auth.threshold + "";
            } else if ("owner".equals(permissionsBean.perm_name)) {
                blockAccountView.blockAccountOwnerKey.setText(permissionsBean.required_auth.keys.get(0).key);
                ownerThreashold = permissionsBean.required_auth.threshold + "";
            }
        }
        blockAccountView.blockAccountThreshold.setText("owner:" + ownerThreashold + " active:" + activeThreshold);
        blockAccountView.blockAccountRamUsed.setText("已用：" + accountInfo.ram_usage);
        blockAccountView.blockAccountRamAvailable.setText("可用：" + (accountInfo.total_resources.ram_bytes - accountInfo.ram_usage));
        blockAccountView.blockAccountRamProgress.setProgress((int) (accountInfo.ram_usage / accountInfo.total_resources.ram_bytes));

        blockAccountView.blockAccountCpuUsed.setText("已用：" + accountInfo.cpu_limit.used);
        blockAccountView.blockAccountCpuAvailable.setText("可用：" + accountInfo.cpu_limit.available + "");
        blockAccountView.blockAccountCpuProgress.setProgress((int) (accountInfo.cpu_limit.used / accountInfo.cpu_limit.max));

        blockAccountView.blockAccountNetUsed.setText("已用：" + accountInfo.net_limit.used + "");
        blockAccountView.blockAccountNetAvailable.setText("可用：" + accountInfo.net_limit.available + "");
        blockAccountView.blockAccountNetProgress.setProgress((int) (accountInfo.net_limit.used / accountInfo.net_limit.max));

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
        @BindView(R.id.block_account_owner_key)
        TextView blockAccountOwnerKey;
        @BindView(R.id.block_account_active_key)
        TextView blockAccountActiveKey;
        @BindView(R.id.block_account_threshold)
        TextView blockAccountThreshold;
        @BindView(R.id.block_account_ram_used)
        TextView blockAccountRamUsed;
        @BindView(R.id.block_account_ram_available)
        TextView blockAccountRamAvailable;
        @BindView(R.id.block_account_ram_progress)
        ProgressBar blockAccountRamProgress;
        @BindView(R.id.block_account_cpu_used)
        TextView blockAccountCpuUsed;
        @BindView(R.id.block_account_cpu_available)
        TextView blockAccountCpuAvailable;
        @BindView(R.id.block_account_cpu_progress)
        ProgressBar blockAccountCpuProgress;
        @BindView(R.id.block_account_net_used)
        TextView blockAccountNetUsed;
        @BindView(R.id.block_account_net_available)
        TextView blockAccountNetAvailable;
        @BindView(R.id.block_account_net_progress)
        ProgressBar blockAccountNetProgress;

        //        @BindView(R.layout.account_view)
        AccountView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class TransactionView {
        @BindView(R.id.block_transfer_id)
        TextView blockTransferId;
        @BindView(R.id.block_transfer_time)
        TextView blockTransferTime;
        @BindView(R.id.block_transfer_act_account)
        TextView blockTransferActAccount;
        @BindView(R.id.block_transfer_act_amount)
        TextView blockTransferActAmount;
        @BindView(R.id.block_transfer_type)
        TextView blockTransferType;
        @BindView(R.id.block_transfer_from)
        TextView blockTransferFrom;
        @BindView(R.id.block_transfer_to)
        TextView blockTransferTo;
        @BindView(R.id.block_transfer_content)
        TextView blockTransferContent;
        @BindView(R.id.block_transfer_memo)
        TextView blockTransferMemo;
        @BindView(R.id.block_transfer_transfer_layout)
        LinearLayout blockTransferLayout;
        @BindView(R.id.block_transfer_origin_data)
        TextView blockTransferOriginData;

        //        @BindView(R.layout.block_transaction)
        TransactionView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class BlockView {
        @BindView(R.id.block_block_height)
        TextView blockBlockHeight;
        @BindView(R.id.block_block_id)
        TextView blockBlockId;
        @BindView(R.id.block_block_time)
        TextView blockBlockTime;
        @BindView(R.id.block_block_spot)
        TextView blockBlockSpot;
        @BindView(R.id.block_block_spot_sign)
        TextView blockBlockSpotSign;
        @BindView(R.id.block_block_transactions)
        RecyclerView blockBlockTransactions;

        BlockView(View view) {
            ButterKnife.bind(this, view);
        }
//        @BindView(R.layout.block_view) }
    }

    @Override
    public void showError(String message, int res) {
        if (!hasView) showEmpty(message);
    }

}
