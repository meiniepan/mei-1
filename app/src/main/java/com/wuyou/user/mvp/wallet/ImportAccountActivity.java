package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        setTitleText(getString(R.string.import_account));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_import_account;
    }


    @OnClick(R.id.btn_import)
    public void onViewClicked() {
        String regex = "[a-z]([a-z]|[1-5]){11}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(importAccountName.getText().toString());
        if (!m.matches()) {
            tvPkError.setText("账户名称格式不正确");
            tvPkError.setVisibility(View.VISIBLE);
            return;
        }
        if (importAccountPk.length() == 0) {
            tvPkError.setVisibility(View.VISIBLE);
            tvPkError.setText("私钥格式不正确");
            return;
        }
        String account = importAccountName.getText().toString().trim();
        List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
        for (EosAccount eosAccount : allEosAccount) {
            if (TextUtils.equals(account, eosAccount.getName())) {
                ToastUtils.ToastMessage(getCtx(), "账号已存在");
                return;
            }
        }
        startImport(account);
    }

    EosPrivateKey privateKey;

    private void startImport(String account) {
        showLoadingDialog();
        EoscDataManager.getIns().readAccountInfo(account).subscribeOn(Schedulers.io())
                .map(eosAccountInfo -> eosAccountInfo.permissions.get(0).required_auth.keys.get(0).key)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> {
                    String pk = importAccountPk.getText().toString().trim();
                    privateKey = new EosPrivateKey(pk); //check pk is illegal and maybe throw exception
                })
                .subscribeWith(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String publicKey) {
                        String pk = importAccountPk.getText().toString().trim();
                        if (TextUtils.equals(privateKey.getPublicKey().toString(), publicKey)) {
                            saveAccount(account, publicKey, pk);
                            Intent intent = new Intent(getCtx(), ScoreAccountActivity.class);
                            intent.putExtra(Constant.IMPORT_ACCOUNT, account);
                            startActivity(intent);
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
        eosAccount.setMain(false);
        eosAccount.setPublicKey(publicKey);
        eosAccount.setPrivateKey(pk);
        eosAccount.setName(account);
        CarefreeDaoSession.getInstance().getEosDao().insertOrReplace(eosAccount);
        CarefreeDaoSession.getInstance().setMainAccount(eosAccount);
        ToastUtils.ToastMessage(getCtx(), "导入成功");
        finish();
    }
}
