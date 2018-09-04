package com.wuyou.user.mvp.wallet;

import android.util.Log;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.local.db.EosAccountDao;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/9/3.
 */

public class WalletPresenter extends WalletContract.Presenter {

    @Override
    public void signUp() {
        addDisposable(EoscDataManager.getIns().getDailyRewords().compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        mView.signUpSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                }));
    }

    @Override
    void createAccount(String account, String phone) {
        EosPrivateKey key = new EosPrivateKey();
        addDisposable(EoscDataManager.getIns().createAccount(phone, account, key.getPublicKey().toString())
                .compose(RxUtil.switchSchedulers())
                .subscribeOn(Schedulers.io())
                .doOnNext(jsonObject -> {
                    EosAccountDao eosDao = CarefreeDaoSession.getInstance().getEosDao();
                    EosAccount mainAccount = CarefreeDaoSession.getInstance().getMainAccount();
                    if (mainAccount != null) {
                        mainAccount.setMain(false);
                        eosDao.update(mainAccount);
                    }
                    EosAccount eosAccount = new EosAccount();
                    eosAccount.setMain(true);
                    eosAccount.setPublicKey(key.getPublicKey().toString());
                    eosAccount.setPrivateKey(key.toWif());
                    eosAccount.setName(account);
                    eosDao.save(eosAccount);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        mView.createAccountSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                }));
    }

    @Override
    void getWalletInfo() {
        EoscDataManager.getIns().readAccountInfo(CarefreeDaoSession.getInstance().getMainAccount().getName())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {

                    }
                });

    }

    @Override
    void getPointRecord() {

    }

    public void transfer() {
        EoscDataManager.getIns().transfer("houjingnan35", "mukangmukang", 1, "111")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "accept: " + CommonUtil.prettyPrintJson(jsonObject));
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        Log.e("Carefree", "onFail: " + e.getDisplayMessage());
                    }
                });
    }

}
