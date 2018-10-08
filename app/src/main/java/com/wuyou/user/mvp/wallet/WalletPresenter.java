package com.wuyou.user.mvp.wallet;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.local.db.EosAccountDao;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by DELL on 2018/9/3.
 */

public class WalletPresenter extends WalletContract.Presenter {

    /**
     * code : 500
     * message : Internal Service Error
     * error : {"code":3050003,"name":"eosio_assert_message_exception","what":"eosio_assert_message assertion failure","details":[{"message":"assertion failure with message: The same phone  number have existed","file":"wasm_interface.cpp","line_number":930,"method":"eosio_assert"},{"message":"pending console output: ","file":"apply_context.cpp","line_number":61,"method":"exec_one"}]}
     */

    public int code;
    public String message;

    @Override
    public void createAccount(String account, String phone) {
        EosPrivateKey key = new EosPrivateKey();
        addDisposable(EoscDataManager.getIns().createAccount(phone, account, key.getPublicKey().toString())
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
                    eosDao.insertOrReplace(eosAccount);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        mView.createAccountSuccess();
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        super.onNodeFail(code, message);
                        if (message.message.contains("number have existed")){
                            mView.showError("您的手机号已创建过账户，无法再创建",code);
                        }else if (message.message.contains("name is already taken")){
                            mView.showError("该账户名称已存在",code);
                        }else {
                            mView.showError(message.message,code);
                        }
                    }
                }));
    }


    @Override
    void getWalletInfo() {
        EoscDataManager.getIns().readAccountInfo(CarefreeDaoSession.getInstance().getMainAccount().getName())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<EosAccountInfo>() {
                    @Override
                    public void onSuccess(EosAccountInfo jsonObject) {

                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError("您的手机号已创建过账户，无法再创建", e.getCode());
                    }
                });
    }

    @Override
    void getCaptcha(String type, String phone) {
        addDisposable(CarefreeRetrofit.getInstance().createApi(UserApis.class).getCaptchaForCheck(type, QueryMapBuilder.getIns().put("mobile", phone).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse userInfoBaseResponse) {
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), Constant.GET_CAPTCHA_FAIL);
                    }
                }));
    }

    @Override
    void checkCaptcha(String type, String phone, String captcha) {
        addDisposable(CarefreeRetrofit.getInstance().createApi(UserApis.class).checkCaptcha(type, QueryMapBuilder.getIns().put("mobile", phone).put("captcha", captcha).buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse o) {
                        mView.checkCaptchaSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                }));
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
