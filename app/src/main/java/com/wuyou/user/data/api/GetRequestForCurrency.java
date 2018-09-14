package com.wuyou.user.data.api;

import com.google.gson.annotations.Expose;

import com.wuyou.user.data.types.TypeName;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by swapnibble on 2018-04-16.
 */
public class GetRequestForCurrency {
    @Expose
    protected TypeName code;

    @Expose
    protected String symbol;

    public GetRequestForCurrency(String tokenContract, String symbol){
        this.code = new TypeName(tokenContract);
        this.symbol = StringUtils.isEmpty(symbol) ? null : symbol;
    }
}
