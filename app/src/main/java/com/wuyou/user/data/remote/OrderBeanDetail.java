package com.wuyou.user.data.remote;

import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderBeanDetail extends OrderBean {

    /**
     * order_id : 5
     * order_number : 201803190710581086238049
     * created_at : 1521443458
     * service_mode : 上门服务
     * service_date : 20180319
     * service_time : 08
     * remark : jhhh
     * status : 1
     * pay_type :
     * serial :
     * pay_time : 0
     * total_amount : 980
     * number : 1
     * shop : {"shop_id":2,"shop_name":"重庆鸡公煲"}
     * service : {"service_id":1,"service_name":"空调清洗","photo":"http://images4.5maiche.cn/2016-07-11_57833334133a7.jpg","specification":[],"price":0,"visiting_fee":0}
     * address : {"address_id":23,"username":"张三","city":"北京市","district":"西城区","area":"庄胜广场南馆","address":"燃气热水器","mobile":"13666666666","lng":"116.375622","lat":"39.896681"}
     */
    public long created_at;
    public String service_mode;
    public String service_date;
    public String service_time;
    public String remark;
    public String pay_type;
    public long pay_time;
    public long second_pay_time;
    public float total_amount;
    public int number;
    public AddressBean address;
    public List<OrderDetailServeBean> services;
}
