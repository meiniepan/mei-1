/*
 * Copyright (c) 2017-2018 PLACTAL.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wuyou.user.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.data.prefs.PreferencesHelper;
import com.wuyou.user.data.remote.NodeosApi;
import com.wuyou.user.data.remote.model.abi.EosAbiMain;
import com.wuyou.user.data.remote.model.api.AccountInfoRequest;
import com.wuyou.user.data.remote.model.api.EosChainInfo;
import com.wuyou.user.data.remote.model.api.GetBalanceRequest;
import com.wuyou.user.data.remote.model.api.GetCodeRequest;
import com.wuyou.user.data.remote.model.api.GetCodeResponse;
import com.wuyou.user.data.remote.model.api.GetRequestForCurrency;
import com.wuyou.user.data.remote.model.api.GetRequiredKeys;
import com.wuyou.user.data.remote.model.api.JsonToBinRequest;
import com.wuyou.user.data.remote.model.api.PushTxnResponse;
import com.wuyou.user.data.remote.model.chain.Action;
import com.wuyou.user.data.remote.model.chain.PackedTransaction;
import com.wuyou.user.data.remote.model.chain.SignedTransaction;
import com.wuyou.user.data.remote.model.types.EosNewAccount;
import com.wuyou.user.data.remote.model.types.EosTransfer;
import com.wuyou.user.data.remote.model.types.TypeChainId;
import com.wuyou.user.data.util.Utils;
import com.wuyou.user.data.wallet.EosWalletManager;
import com.wuyou.user.network.ChainRetrofit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by swapnibble on 2017-11-03.
 */
public class EoscDataManager {
    private static EoscDataManager eoscDataManager;

    public synchronized static EoscDataManager getIns() {
        if (eoscDataManager == null) {
            eoscDataManager = new EoscDataManager();
        }
        return eoscDataManager;
    }

    private final PreferencesHelper mPrefHelper;
    private final EosWalletManager mWalletMgr;

    private EoscDataManager() {
        mWalletMgr = new EosWalletManager();
        mPrefHelper = new PreferencesHelper(CarefreeApplication.getInstance().getApplicationContext());
    }

    public EosWalletManager getWalletManager() {
        return mWalletMgr;
    }

    public PreferencesHelper getPreferenceHelper() {
        return mPrefHelper;
    }


    public void addAccountHistory(String... accountNames) {
        CarefreeDaoSession.getInstance().addAll(accountNames);
    }

    public void addAccountHistory(List<String> accountNames) {
        CarefreeDaoSession.getInstance().addAll(accountNames);
    }

    public void deleteAccountHistory(String accountName) {
        CarefreeDaoSession.getInstance().delete(accountName);
    }

    public List<EosAccount> searchAccount(String nameStarts) {
        return CarefreeDaoSession.getInstance().searchName(nameStarts);
    }

    public Observable<EosChainInfo> getChainInfo() {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).readInfo("get_info");
    }

//    public Observable<String> getTable(String accountName, String code, String table,
//                                       String tableKey, String lowerBound, String upperBound, int limit ){
//        return mNodeosApi.getTable(
//                new GetTableRequest(accountName, code, table, tableKey, lowerBound, upperBound, limit))
//                .map( tableResult -> Utils.prettyPrintJson(tableResult));
//    }

    public Observable<EosPrivateKey[]> createKey(int count) {
        return Observable.fromCallable(() -> {
            EosPrivateKey[] retKeys = new EosPrivateKey[count];
            for (int i = 0; i < count; i++) {
                retKeys[i] = new EosPrivateKey();
            }

            return retKeys;
        });
    }


    private SignedTransaction createTransaction(String contract, String actionName, String dataAsHex,
                                                String[] permissions, EosChainInfo chainInfo) {
        currentBlockInfo = chainInfo;
        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData(dataAsHex);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(action);
        txn.putSignatures(new ArrayList<>());


        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(Constant.TX_EXPIRATION_IN_MILSEC));
        }

        return txn;
    }

    private Observable<PackedTransaction> signAndPackTransaction(SignedTransaction txnBeforeSign) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getRequiredKeys(new GetRequiredKeys(txnBeforeSign, mWalletMgr.listPubKeys()))
                .map(keys -> {
                    final SignedTransaction stxn;
                    if (mPrefHelper.shouldSkipSigning()) {
                        stxn = txnBeforeSign;
                    } else {
                        stxn = mWalletMgr.signTransaction(txnBeforeSign, keys.getKeys(), new TypeChainId(currentBlockInfo.getChain_id()));
                    }
                    return new PackedTransaction(stxn);
                });
    }

    private String[] getActivePermission(String accountName) {
        return new String[]{accountName + "@active"};
    }


    public Observable<JsonObject> readAccountInfo(String accountName) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getAccountInfo(new AccountInfoRequest(accountName));
    }


    public Observable<JsonObject> getTransactions(String accountName) {

        JsonObject gsonObject = new JsonObject();
        gsonObject.addProperty(NodeosApi.GET_TRANSACTIONS_KEY, accountName);

        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getAccountHistory(NodeosApi.ACCOUNT_HISTORY_GET_TRANSACTIONS, gsonObject);
    }

    public Observable<JsonObject> getServants(String accountName) {

        JsonObject gsonObject = new JsonObject();
        gsonObject.addProperty(NodeosApi.GET_SERVANTS_KEY, accountName);

        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getAccountHistory(NodeosApi.ACCOUNT_HISTORY_GET_SERVANTS, gsonObject);
    }

    EosChainInfo currentBlockInfo;

    void setInfo(EosChainInfo info) {
        currentBlockInfo = info;
    }

    public Observable<PushTxnResponse> createAccount(EosNewAccount newAccountData) {
        return getChainInfo()
                .map(info -> createTransaction(Constant.EOSIO_SYSTEM_ACCOUNT, newAccountData.getActionName(), newAccountData.getAsHex()
                        , getActivePermission(newAccountData.getCreatorName()), info))
                .flatMap(txn -> signAndPackTransaction(txn))
                .flatMap(packedTxn -> ChainRetrofit.getInstance().createApi(NodeosApi.class).pushTransaction(packedTxn));
    }

    public Observable<JsonObject> transfer(String from, String to, long amount, String memo) {
        EosTransfer transfer = new EosTransfer(from, to, amount, memo);
        return pushActionRetJson(Constant.EOSIO_TOKEN_CONTRACT, transfer.getActionName(), Utils.prettyPrintJson(transfer), getActivePermission(from)); //transfer.getAsHex()
    }

    public Observable<JsonObject> pushActionRetJson(String contract, String action, String data, String[] permissions) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).jsonToBin(new JsonToBinRequest(contract, action, data))
                .flatMap(jsonToBinResp -> getChainInfo()
                        .map(info -> createTransaction(contract, action, jsonToBinResp.getBinargs(), permissions, info)))
                .flatMap(this::signAndPackTransaction)
                .flatMap(ChainRetrofit.getInstance().createApi(NodeosApi.class)::pushTransactionRetJson);
    }

    public Observable<PushTxnResponse> pushAction(String contract, String action, String data, String[] permissions) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).jsonToBin(new JsonToBinRequest(contract, action, data))
                .flatMap(jsonToBinResp -> getChainInfo()
                        .map(info -> createTransaction(contract, action, jsonToBinResp.getBinargs(), permissions, info)))
                .flatMap(this::signAndPackTransaction)
                .flatMap(ChainRetrofit.getInstance().createApi(NodeosApi.class)::pushTransaction);
    }

    public Observable<EosAbiMain> getCodeAbi(String contract) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getCode(new GetCodeRequest(contract))
                .filter(GetCodeResponse::isValidCode)
                .map(result -> new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                        .create().fromJson(result.getAbi(), EosAbiMain.class));
    }

    public Observable<EosAbiMain> getAbiMainFromJson(String jsonStr) {
        return Observable.just(new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create().fromJson(jsonStr, EosAbiMain.class));
    }

    public Observable<String> getCurrencyBalance(String contract, String account, String symbol) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getCurrencyBalance(new GetBalanceRequest(contract, account, symbol))
                .map(result -> Utils.prettyPrintJson(result));
    }

    public Observable<String> getCurrencyStats(String contract, String symbol) {
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getCurrencyStats(new GetRequestForCurrency(contract, symbol))
                .map(result -> Utils.prettyPrintJson(result));
    }
}
