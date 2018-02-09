package com.wuyou.user.bean;

import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderBeanDetail{
    public String order_id;
    public String category;
    public long created_at;
    public String status;
    public long updated_at;
    public String shop_id;
    public String shop_name;
    public String shop_tel;
    public String nums;
    public String price;
    public List<OrderPreferentialBean> preferentials;
    public String contact_address;
    public String contact_name;
    public String contact_tel;
    public String service_time;
    public String order_no;
    public String payment;
    public String pay_type;
    public String pay_status;
}
