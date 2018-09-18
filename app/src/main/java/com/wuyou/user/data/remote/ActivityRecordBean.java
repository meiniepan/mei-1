package com.wuyou.user.data.remote;

import com.wuyou.user.data.remote.response.BaseItemBean;

import java.util.List;

/**
 * Created by DELL on 2018/9/17.
 */

public class ActivityRecordBean extends BaseItemBean {

    /**
     * order_id : 10
     * price : 10000
     * fee_type : [1,2,3]
     * status : 0
     * activity : {"activity_id":1,"title":"活动 - test","start_at":1526545800,"end_at":1526592600,"image":["http://image.iwantmei.com/171445_bMIZ_1428332.jpg"],"fee":[{"id":1,"name":"午餐","price":0},{"id":2,"name":"停车","price":0},{"id":3,"name":"座位","price":0}]}
     */

    public float price;
    public int status;
    public ActivityBean activity;
    public List<Integer> fee_type;

    public static class ActivityBean {
        /**
         * activity_id : 1
         * title : 活动 - test
         * start_at : 1526545800
         * end_at : 1526592600
         * image : ["http://image.iwantmei.com/171445_bMIZ_1428332.jpg"]
         * fee : [{"id":1,"name":"午餐","price":0},{"id":2,"name":"停车","price":0},{"id":3,"name":"座位","price":0}]
         */

        public String activity_id;
        public String title;
        public long start_at;
        public long end_at;
        public List<String> image;
    }
}
