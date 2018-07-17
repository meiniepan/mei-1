package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderBean implements Parcelable {

    /**
     * order_id : 5
     * order_number : 201803190710581086238049
     * status : 1. 待付款 2.进行中 3.待评价 4已完成 5已取消
     * amount : 980
     * service : {"service_id":1,"service_name":"空调清洗","photo":"http://images4.5maiche.cn/2016-07-11_57833334133a7.jpg"}
     * shop : {"shop_id":2,"shop_name":"重庆鸡公煲"}
     */

    public String order_id;
    public String order_number;
    public int status;
    public String serial;
    public float amount;
    public ServeBean service;
    public ShopBean shop;
    public int can_finish;

    public OrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.order_number);
        dest.writeInt(this.status);
        dest.writeString(this.serial);
        dest.writeFloat(this.amount);
        dest.writeParcelable(this.service, flags);
        dest.writeParcelable(this.shop, flags);
        dest.writeInt(this.can_finish);
    }

    protected OrderBean(Parcel in) {
        this.order_id = in.readString();
        this.order_number = in.readString();
        this.status = in.readInt();
        this.serial = in.readString();
        this.amount = in.readFloat();
        this.service = in.readParcelable(ServeBean.class.getClassLoader());
        this.shop = in.readParcelable(ShopBean.class.getClassLoader());
        this.can_finish = in.readInt();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
