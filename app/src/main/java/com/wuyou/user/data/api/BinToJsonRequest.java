package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/24.
 */

public class BinToJsonRequest {

    private String code;

    private String action;

    public BinToJsonRequest(String code, String action, String binargs) {
        this.code = code;
        this.action = action;
        this.binargs = binargs;
    }

    private String binargs;
}
