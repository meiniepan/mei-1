package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2018/2/7.
 */

public class ServeDetailBean implements Parcelable {
    public String id;
    public String category_name;
    public String image;
    public String price;
    public String total_sell;
    public String service_time;
    public String description;
//    public List<String> notice;
//    public String securities; //保障????
    public String name;
    public String shop_id;
    public String other_price;
    public String level;
    public String service_id;
    public boolean selected;
    public int repertory;

    public ServeDetailBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category_name);
        dest.writeString(this.image);
        dest.writeString(this.price);
        dest.writeString(this.total_sell);
        dest.writeString(this.service_time);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.shop_id);
        dest.writeString(this.other_price);
        dest.writeString(this.level);
        dest.writeString(this.service_id);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected ServeDetailBean(Parcel in) {
        this.id = in.readString();
        this.category_name = in.readString();
        this.image = in.readString();
        this.price = in.readString();
        this.total_sell = in.readString();
        this.service_time = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.shop_id = in.readString();
        this.other_price = in.readString();
        this.level = in.readString();
        this.service_id = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<ServeDetailBean> CREATOR = new Creator<ServeDetailBean>() {
        @Override
        public ServeDetailBean createFromParcel(Parcel source) {
            return new ServeDetailBean(source);
        }

        @Override
        public ServeDetailBean[] newArray(int size) {
            return new ServeDetailBean[size];
        }
    };
}
