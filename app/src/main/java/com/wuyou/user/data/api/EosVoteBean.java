package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/11.
 */

public class EosVoteBean {
    public String id;
    public String voter;
    public List<VoteOption> option;

    public EosVoteBean(String id, String voter, List<VoteOption> option) {
        this.id = id;
        this.option = option;
        this.voter  =voter;
    }

    public EosVoteBean() {

    }

    public String getActionName() {
        return "voteactivity";
    }

}
