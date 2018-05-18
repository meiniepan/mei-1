package com.wuyou.user.bean;

/**
 * Created by DELL on 2018/5/11.
 */

public class JSBean {
    public String activityid;
    public String cbname;
    public String methodname;
    public String order_id;
    public String activityUrl;
    public String activityTitle;

    @Override
    public String toString() {
        return "JSBean{" +
                "activityid='" + activityid + '\'' +
                ", cbname='" + cbname + '\'' +
                ", methodname='" + methodname + '\'' +
                ", order_id='" + order_id + '\'' +
                ", activityUrl='" + activityUrl + '\'' +
                ", activityTitle='" + activityTitle + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                '}';
    }

    public String ImgUrl;
//    public String UserLogin;
//    public String AppGoBack;
//    public String ShareActivity;
//    public String GoApplayPage;

}
