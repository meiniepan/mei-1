package com.wuyou.user.data.local;

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
    public String Address;

    @Override
    public String toString() {
        return "JSBean{" +
                "ActivityId='" + ActivityId + '\'' +
                ", CallBackName='" + CallBackName + '\'' +
                ", MethodName='" + MethodName + '\'' +
                ", OrderId='" + OrderId + '\'' +
                ", ActivityUrl='" + ActivityUrl + '\'' +
                ", ActivityTitle='" + ActivityTitle + '\'' +
                ", Address='" + Address + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                '}';
    }

    public String ImgUrl;
//    public String UserLogin;
//    public String AppGoBack;
//    public String ShareActivity;
//    public String GoApplayPage;

}
