package com.wuyou.user.mvp.vote;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosVoteListBean;
import com.wuyou.user.data.api.VoteOption;
import com.wuyou.user.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/10/11.
 */

public class VotePresenter {

    public void getUserVoteLis() {
        EoscDataManager.getIns().getTable("ayiuivl52fnq", "votevotevote", "infos")
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                EosVoteListBean listBean = new GsonBuilder().create().fromJson(s, EosVoteListBean.class);
                Log.e("Carefree", "onSuccess: " + listBean.rows);
            }
        });
    }

    public void getAllVoteList() {
        EoscDataManager.getIns().getTable("votevotevote", "votevotevote", "votelist","end_time","","",20)
                .compose(RxUtil.switchSchedulers()).subscribe(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                EosVoteListBean listBean = new GsonBuilder().create().fromJson(s, EosVoteListBean.class);
                Log.e("Carefree", "onSuccess: " + listBean.rows);
            }
        });
    }

    public void doVote(String id, ArrayList<VoteOption> list,int num) {
//        List<Integer> optList = new ArrayList();
//        optList.add(1);
//        VoteOption voteOption = new VoteOption(optList);
//        ArrayList<VoteOption> list = new ArrayList<>();
//        list.add(voteOption);
        EoscDataManager.getIns().doVote(id, list,num)
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
