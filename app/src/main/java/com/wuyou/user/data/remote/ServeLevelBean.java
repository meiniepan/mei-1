package com.wuyou.user.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/19.
 */

public class ServeLevelBean implements Parcelable {

    /**
     * shop_id : 114
     * price : 100.0
     * other_price : 5.0
     * service_time : 06:00 - 23:00
     * level : 钻石服务
     * service_id : 117
     * description : 钻石服务描述
     * background : http://img3.imgtn.bdimg.com/it/u=439339977,98971476&fm=27&gp=0.jpg
     */

    public String shop_id;
    public String price;
    public String other_price;
    public String level;
    public String service_id;
    public String description;
    public String background;
    public String visiting_fee;
    public boolean selected;
    public String start_at;
    public String end_at;

    public ServeLevelBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_id);
        dest.writeString(this.price);
        dest.writeString(this.other_price);
        dest.writeString(this.level);
        dest.writeString(this.service_id);
        dest.writeString(this.description);
        dest.writeString(this.background);
        dest.writeString(this.visiting_fee);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.start_at);
        dest.writeString(this.end_at);
    }

    protected ServeLevelBean(Parcel in) {
        this.shop_id = in.readString();
        this.price = in.readString();
        this.other_price = in.readString();
        this.level = in.readString();
        this.service_id = in.readString();
        this.description = in.readString();
        this.background = in.readString();
        this.visiting_fee = in.readString();
        this.selected = in.readByte() != 0;
        this.start_at = in.readString();
        this.end_at = in.readString();
    }

    public static final Creator<ServeLevelBean> CREATOR = new Creator<ServeLevelBean>() {
        @Override
        public ServeLevelBean createFromParcel(Parcel source) {
            return new ServeLevelBean(source);
        }

        @Override
        public ServeLevelBean[] newArray(int size) {
            return new ServeLevelBean[size];
        }
    };
}
