package com.wuyou.user.mvp.trace;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.network.ipfs.ChainIPFS;
import com.wuyou.user.network.ipfs.ChainNamedStreamable;
import com.wuyou.user.util.RxUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DELL on 2018/10/23.
 */

public class TracePresenter {

    public void uploadTrace(String content, List<String> pictureHashList, int amount) {
        TraceIPFSBean bean = new TraceIPFSBean();
        bean.account_name = CarefreeDaoSession.getInstance().getMainAccount().getName();
        bean.timestamp = TribeDateUtils.dateFormat7(new Date(System.currentTimeMillis()));
        bean.content = content;
        bean.phone = CarefreeDaoSession.getInstance().getUserInfo().getMobile();
        bean.picture = pictureHashList;
        bean.node_name = "庄胜广场";
        try {
            uploadFileToIpfs(bean, amount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileToIpfs(TraceIPFSBean bean, int amount) throws IOException {
        String desString = new GsonBuilder().create().toJson(bean);
        Observable.create((ObservableOnSubscribe<String>) e -> {
            ChainIPFS ipfs = new ChainIPFS(Constant.IPFS_URL.contains(Constant.BASE_CHAIN_URL) ? Constant.BASE_CHAIN_URL : Constant.DEV_BASE_CHAIN_URL, 5001);
            ipfs.local();
            ChainNamedStreamable file = new ChainNamedStreamable.ByteArrayWrapper(desString.getBytes());
            e.onNext(ipfs.addFile(Collections.singletonList(file)));
        }).flatMap(s -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("file_hash", s);
            return EoscDataManager.getIns().transfer(CarefreeDaoSession.getInstance().getMainAccount().getName(), "wuyoulianqqq", amount, jsonObject.toString());
        }).doOnNext(jsonObject -> CarefreeDaoSession.getInstance().addTraceRecord(bean)).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                    }
                });
    }


    public void getProposalTable() {
        EoscDataManager.getIns().getTable(Constant.EOSIO_TRACE_SCOPE, "samsamsamsam", "proposal")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("Carefree", "getProposalTable onSuccess: " + s);

                    }
                });
    }

    public void getApproveTable() {
        EoscDataManager.getIns().getTable(Constant.EOSIO_TRACE_SCOPE, "samsamsamsam", "approvals")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("Carefree", "getApproveTable onSuccess: " + s);
                    }
                });
    }


}
