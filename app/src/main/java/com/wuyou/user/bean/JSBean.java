package com.wuyou.user.bean;

/**
 * Created by DELL on 2018/5/11.
 */

public class JSBean {
    public String ActivityId;
    public String CallBackName;
    public String MethodName ;
    public String OrderId;
    public String ActivityUrl;
    public String ActivityTitle;

    @Override
    public String toString() {
        return "JSBean{" +
                "ActivityId='" + ActivityId + '\'' +
                ", CallBackName='" + CallBackName + '\'' +
                ", methodname='" + MethodName + '\'' +
                ", order_id='" + OrderId + '\'' +
                ", activityUrl='" + ActivityUrl + '\'' +
                ", ActivityTitle='" + ActivityTitle + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                '}';
    }

    public String ImgUrl;
//    public String UserLogin;
//    public String AppGoBack;
//    public String ShareActivity;
//    public String GoApplayPage;

}
