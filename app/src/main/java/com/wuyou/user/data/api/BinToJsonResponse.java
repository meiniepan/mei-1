package com.wuyou.user.data.api;

import com.wuyou.user.data.types.EosTransfer;

import java.util.List;

/**
 * Created by DELL on 2018/10/24.
 */

public class BinToJsonResponse {

    /**
     * args : {"from":"initb","to":"initc","quantity":1000}
     * required_scope : []
     * required_auth : []
     */

    public EosTransfer args;
    public List<?> required_scope;
    public List<?> required_auth;

}
