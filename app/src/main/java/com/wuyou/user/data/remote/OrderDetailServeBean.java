package com.wuyou.user.data.remote;

/**
 * Created by hjn on 2018/2/6.
 * <p>
 * "service_id": 3,
 * "service_name": "不知道清洗啥",
 * "image": "http://images4.5maiche.cn/2016-07-11_57833334133a7.jpg",
 * "number": 1,
 * "has_specification": 1,
 * "specification": {
 * "id": 1,
 * "name": "原味",
 * "price": 0.01,
 * "stock": 5,
 * "photo": "",
 * "sales": 0
 * },
 * "stage": 2,
 * "amount": 0.03,
 * "visiting_fee": 0,
 * "price": 0.03
 */

public class OrderDetailServeBean {
    public String service_id;
    public String service_name;
    public float price;
    public String sold;
    public float visiting_fee;
    public String shop_name;
    public String image;
    public int has_specification;
    public ServeSpecification specification;
    public int number;
    public float amount;
    public int stage;
}
