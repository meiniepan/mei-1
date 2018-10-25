package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/25.
 */

public class ExecuterBean {
    public String proposer;
    public String executer;
    public String proposal_name;

    public ExecuterBean(String eosioTracePc, String name, String name1) {
        proposer = eosioTracePc;
        executer = name;
        proposal_name = name1;
    }

    public String getActionName() {
        return "exec";
    }
}
