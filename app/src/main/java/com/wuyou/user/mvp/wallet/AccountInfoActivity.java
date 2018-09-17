package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/12.
 */

public class AccountInfoActivity extends BaseActivity {
    @BindView(R.id.tv_account_name_11)
    TextView tvAccountName11;
    @BindView(R.id.tv_account_name_12)
    TextView tvAccountName12;
    @BindView(R.id.tv_account_num)
    TextView tvAccountNum;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.account_info));
        EosAccount mainAccount = CarefreeDaoSession.getInstance().getMainAccount();
        tvAccountName11.setText(mainAccount.getName());
        tvAccountName12.setText(mainAccount.getName());
        getAccountScore();
    }

    public void getAccountScore() {
        EoscDataManager.getIns().getCurrencyBalance(Constant.EOSIO_TOKEN_CONTRACT, CarefreeDaoSession.getInstance().getMainAccount().getName(), "EOS").compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray eosAccountInfo) {
                        if (eosAccountInfo.size() > 0) {
                            String scoreAmount = eosAccountInfo.get(0).toString().replace("EOS", "").replaceAll("\"", "");
                            tvAccountNum.setText(scoreAmount);
                        }
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_backup;
    }

    @OnClick(R.id.back_up)
    public void onViewClicked() {
        Intent intent = new Intent(getCtx(), BackupPKeyActivity.class);
        startActivity(intent);
        finish();
    }
}
