package com.wuyou.user.event;

/**
 * Created by DELL on 2018/4/20.
 */

public class WXPayEvent {
    public int errCode;

    public WXPayEvent(int errCode) {
        this.errCode = errCode;
    }
}
