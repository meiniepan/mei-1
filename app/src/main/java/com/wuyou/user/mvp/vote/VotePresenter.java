package com.wuyou.user.mvp.vote;

import android.util.Log;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VoteOption;
import com.wuyou.user.util.RxUtil;

import java.util.ArrayList;

/**
 * Created by DELL on 2018/10/11.
 */

public class VotePresenter {

    public void getVoteLis() {
        EoscDataManager.getIns().getTable("ayiuivl52fnq", "votevotevote", "infos")
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("Carefree", "onSuccess: " + s);
            }
        });

        EoscDataManager.getIns().getTable("votevotevote", "votevotevote", "votelist")
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("Carefree", "onSuccess: " + s);
            }
        });
    }

    public void doVote() {
        VoteOption voteOption = new VoteOption(new int[]{1});
        ArrayList<VoteOption> list = new ArrayList<>();
        list.add(voteOption);
        EoscDataManager.getIns().doVote("0", list)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("Carefree", "onSuccess: " + jsonObject);
                    }


                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        if (message.message.contains("You have voted")) {
//                            ToastUtils.ToastMessage(getContext(), "您已经投过票了");
                        } else {
//                            ToastUtils.ToastMessage(getContext(), message.message);
                        }
                    }
                });
    }
}
