package com.wuyou.user.data.api;

import com.wuyou.user.data.types.TypeAsset;

import java.util.List;

/**
 * Created by DELL on 2018/10/11.
 */

public class EosVoteBean {
    public String id;
    public String voter;
    public List<VoteOption> option;
    public TypeAsset quant;

    public EosVoteBean(String id, String voter, List<VoteOption> option, int asset) {
        this.id = id;
        this.option = option;
        this.voter = voter;
        this.quant = new TypeAsset(asset);
    }

    public EosVoteBean() {

    }

    public String getActionName() {
        return "voteactivity";
    }

}
