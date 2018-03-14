package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderBean implements Parcelable {
    public String id;
    public String category_name;
    public long created_at;
    public String status;
    public String address;
    public String time_interval;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category_name);
        dest.writeLong(this.created_at);
        dest.writeString(this.status);
        dest.writeString(this.address);
        dest.writeString(this.time_interval);
    }

    public OrderBean() {
    }

    protected OrderBean(Parcel in) {
        this.id = in.readString();
        this.category_name = in.readString();
        this.created_at = in.readLong();
        this.status = in.readString();
        this.address = in.readString();
        this.time_interval = in.readString();
    }

    public static final Parcelable.Creator<OrderBean> CREATOR = new Parcelable.Creator<OrderBean>() {
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
