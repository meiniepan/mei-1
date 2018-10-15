package com.wuyou.user.data.api;

import com.google.gson.annotations.Expose;


/**
 * Created by swapnibble on 2017-09-15.
 */

public class GetTableRequest {
    private static final int DEFAULT_FETCH_LIMIT = 20;

    @Expose
    private boolean json = true;

    @Expose
    private String code;

    @Expose
    private String scope;

    @Expose
    private String table;


    @Expose
    private int limit = DEFAULT_FETCH_LIMIT;


    public GetTableRequest(String scope, String code, String table, String tableKey, String lowerBound, String upperBound, int limit) {
        this.scope = scope;
        this.code = code;
        this.table = table;

        this.limit = limit <= 0 ? DEFAULT_FETCH_LIMIT : limit;
    }

    public GetTableRequest(String scope, String code, String table) {
        this.scope = scope;
        this.code = code;
        this.table = table;
    }
}
