package com.wuyou.user.bean;

import java.util.List;

/**
 * Created by DELL on 2018/8/13.
 */

public class ActivityListBean {
    public String id;
    public String[] image;
    public String title;
    public String address;
    public String date;
    public String user_limit;
    public String surplus; //剩余人数
    public int free;    //是否免费（0：否；1：是）
    public int status; //状态（0：正常；1：已报满；2：已结束）
    public String sort;
    public List<ActivityFee> fee;


    public class ActivityFee {
        public String id;
        public String name;
        public float price;
    }
}
