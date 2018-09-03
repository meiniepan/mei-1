package com.wuyou.user.data.remote;

/**
 * Created by hjn on 2017/7/21.
 */

public enum PayChannel {
    BALANCE("无忧支付") ,ALIPAY("支付宝支付"), WECHAT("微信支付"),BF_BANKCARD("银行卡支付");
   public String value;

    PayChannel(String value) {
        this.value=value;
    }
}
