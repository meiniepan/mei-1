package com.wuyou.user.mvp.volunteer;

import android.util.Log;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.util.RxUtil;

/**
 * Created by DELL on 2018/10/29.
 */

public class TimeBankPresenter extends TimeBankRecordContract.Presenter {


    public void attendVolunteerProject() {
        EoscDataManager.getIns().registerTimeBank("", "", "")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                    }
                });
    }


    @Override
    void registerProject(int position, VolunteerProjectBean bean) {
        EoscDataManager.getIns().registerTimeBank(bean.id + "", bean.creator, bean.name)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                        mView.registerSuccess(position);
                    }
                });
    }

    @Override
    void rewardProject(int position, VolunteerProjectBean bean) {
        EoscDataManager.getIns().registerTimeBank(bean.id + "", bean.creator, bean.name)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                        mView.registerSuccess(position);
                    }
                });
    }

    @Override
    void getRecordData() {
        EoscDataManager.getIns().getTable(CarefreeDaoSession.getInstance().getMainAccount().getName(), Constant.EOS_TIME_BANK, "infos")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("Carefree", "onSuccess: " + s);
                    }
                });
    }
}
