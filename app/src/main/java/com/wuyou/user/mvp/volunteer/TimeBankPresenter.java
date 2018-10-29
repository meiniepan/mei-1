package com.wuyou.user.mvp.volunteer;

import android.util.Log;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.util.RxUtil;

/**
 * Created by DELL on 2018/10/29.
 */

public class TimeBankPresenter {


    public void attendVolunteerProject() {
        EoscDataManager.getIns().registTimeBank("", "", "")
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                    }
                });
    }


}
