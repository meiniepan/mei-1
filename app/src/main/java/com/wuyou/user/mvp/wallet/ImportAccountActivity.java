package com.wuyou.user.mvp.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/10.
 */

public class ImportAccountActivity extends BaseActivity {
    @BindView(R.id.import_account_name)
    EditText importAccountName;
    @BindView(R.id.import_account_pk)
    EditText importAccountPk;
    @BindView(R.id.tv_pk_error)
    TextView tvPkError;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
        setTitleText(getString(R.string.import_account));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_import_account;
    }


    @OnClick(R.id.btn_import)
    public void onViewClicked() {
        if (importAccountName.length() != 12) {
            tvPkError.setVisibility(View.VISIBLE);
            tvPkError.setText("账户名称长度为12位");
            return;
        }
        if (importAccountPk.length() == 0) {
            tvPkError.setVisibility(View.VISIBLE);
            tvPkError.setText("私钥格式不正确");
            return;
        }
        String account = importAccountName.getText().toString().trim();
        EoscDataManager.getIns().readAccountInfo(account).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<EosAccountInfo>() {
                    @Override
                    public void onSuccess(EosAccountInfo eosAccountInfo) {
                        String publicKey = eosAccountInfo.permissions.get(0).required_auth.keys.get(0).key;
                        String pk = importAccountPk.getText().toString().trim();
                        EosPrivateKey privateKey = new EosPrivateKey(pk);
                        if (TextUtils.equals(privateKey.getPublicKey().toString(), publicKey)) {
                            saveAccount(account, publicKey, pk);
                        } else {
                            tvPkError.setText("私钥或账户名称不正确");
                            tvPkError.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        tvPkError.setText("私钥或账户名称不正确");
                        tvPkError.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void saveAccount(String account, String publicKey, String pk) {
        EosAccount eosAccount = new EosAccount();
        if (CarefreeDaoSession.getInstance().getAllEosAccount().size() == 0) {
            eosAccount.setMain(true);
        }
        eosAccount.setPublicKey(publicKey);
        eosAccount.setPrivateKey(pk);
        eosAccount.setName(account);
        CarefreeDaoSession.getInstance().getEosDao().insertOrReplace(eosAccount);
        ToastUtils.ToastMessage(getCtx(), "导入成功");
        finish();
    }
}
