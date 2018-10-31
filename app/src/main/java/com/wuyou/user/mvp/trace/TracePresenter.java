package com.wuyou.user.mvp.trace;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.BinToJsonRequest;
import com.wuyou.user.data.api.ProposalRows;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.network.ChainRetrofit;
import com.wuyou.user.network.IPFSRetrofit;
import com.wuyou.user.network.apis.NodeosApi;
import com.wuyou.user.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/10/23.
 */

public class TracePresenter extends TraceContract.Presenter {

    public void getProposalTableData() {
        EoscDataManager.getIns().getTable(Constant.EOSIO_TRACE_PC, Constant.EOSIO_TRACE_SCOPE, "proposal")
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    ArrayList<ObservableSource<TraceIPFSBean>> observableArrayList = new ArrayList<>();
                    ProposalRows proposalRows = new GsonBuilder().create().fromJson(s, ProposalRows.class);
                    for (ProposalRows.RowsBean bean : proposalRows.rows) {
                        if (TextUtils.equals(bean.proposal_name, CarefreeDaoSession.getInstance().getMainAccount().getName())) {
                            observableArrayList.add(getJsonData(bean.packed_transaction));
                        }
                    }
                    return observableArrayList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(observableSources -> {
                    if (observableSources.size() == 0) {
                        mView.getAuthDataSuccess(Collections.emptyList());
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(observableSources -> Observable.zip(observableSources, traceIPFSBeans -> {
                    ArrayList<TraceIPFSBean> beans = new ArrayList<>();
                    for (Object object : traceIPFSBeans) {
                        beans.add((TraceIPFSBean) object);
                    }
                    return beans;
                }))
                .doOnNext(beans -> {
                    for (TraceIPFSBean bean : beans) {
                        TraceIPFSBean traceBean = CarefreeDaoSession.getInstance().findTraceBean(bean);
                        if (traceBean != null) {
                            traceBean.setStatus(0);
                        }
                        CarefreeDaoSession.getInstance().updateTraceBean(traceBean);
                    }
                })
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<ArrayList<TraceIPFSBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TraceIPFSBean> beans) {
                        mView.getAuthDataSuccess(beans);
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        mView.showError(message.message, code);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(),e.getCode());
                    }
                });
    }

    private Observable<TraceIPFSBean> getJsonData(String packed_transaction) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("packed_trx", packed_transaction);
        return ChainRetrofit.getInstance().createApi(NodeosApi.class).getDataForPrx(jsonObject)
                .flatMap(jsonObject1 -> {
                    JsonElement data = jsonObject1.get("data");
                    return ChainRetrofit.getInstance().createApi(NodeosApi.class).getBeanFromData(new BinToJsonRequest(Constant.EOSIO_TOKEN_CONTRACT, "transfer", data.getAsString()));
                })
                .flatMap(binToJsonResponse -> IPFSRetrofit.getInstance().createApi(NodeosApi.class).getIPFSData(binToJsonResponse.args.memo))
                .flatMap(new Function<JsonObject, ObservableSource<TraceIPFSBean>>() {
                    @Override
                    public ObservableSource<TraceIPFSBean> apply(JsonObject s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<TraceIPFSBean>() {
                            @Override
                            public void subscribe(ObservableEmitter<TraceIPFSBean> e) throws Exception {
                                e.onNext(new Gson().fromJson(s, TraceIPFSBean.class));
                            }
                        });
                    }
                });
    }

    public void getApproveTable() {
        EoscDataManager.getIns().getTable("samsamsamsam", Constant.EOSIO_TRACE_SCOPE, "approvals")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("Carefree", "getApproveTable onSuccess: " + s);
                    }
                });
    }

    @Override
    void approveAndExec(int position, TraceIPFSBean bean) {
        EoscDataManager.getIns().doTraceApprove()
                .flatMap(jsonObject -> EoscDataManager.getIns().doTraceExec())
                .doOnNext(jsonObject -> {
                    TraceIPFSBean traceBean = CarefreeDaoSession.getInstance().findTraceBean(bean);
                    traceBean.setStatus(1);
                    CarefreeDaoSession.getInstance().updateTraceBean(traceBean);
                })
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        mView.approveAndExecSuccess(position);
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        ToastUtils.ToastMessage(CarefreeApplication.getInstance().getApplicationContext(), message.message.contains("transaction expired") ? "已过期，等待后台处理" : message.message);
                    }
                });
    }

    @Override
    void getData(int position) {
        if (position == 0) {
            mView.getAuthDataSuccess(CarefreeDaoSession.getInstance().getAllUnAuthTraceRecord());
        } else if (position == 1) {
            getProposalTableData();
        } else {
            mView.getAuthDataSuccess(CarefreeDaoSession.getInstance().getAllFinishedTraceRecord());
        }
    }
}
