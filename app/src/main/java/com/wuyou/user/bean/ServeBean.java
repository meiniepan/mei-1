package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class ServeBean implements Parcelable {
    public String service_id;
    public String shop_id;
    public String category;
    public String service_name;
    public float price;
    public String sold;
    public String visiting_fee;
    public String lng;
    public String lat;
    public String high_praise;
    public int star;
    public String shop_name;
    public String photo;
    public String image;
    public String unit;
    public int has_specification;
    public List<ServeStandard> specification;

    public ServeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.service_id);
        dest.writeString(this.shop_id);
        dest.writeString(this.category);
        dest.writeString(this.service_name);
        dest.writeFloat(this.price);
        dest.writeString(this.sold);
        dest.writeString(this.visiting_fee);
        dest.writeString(this.lng);
        dest.writeString(this.lat);
        dest.writeString(this.high_praise);
        dest.writeInt(this.star);
        dest.writeString(this.shop_name);
        dest.writeString(this.photo);
        dest.writeString(this.image);
        dest.writeString(this.unit);
        dest.writeInt(this.has_specification);
        dest.writeTypedList(this.specification);
    }

    protected ServeBean(Parcel in) {
        this.service_id = in.readString();
        this.shop_id = in.readString();
        this.category = in.readString();
        this.service_name = in.readString();
        this.price = in.readFloat();
        this.sold = in.readString();
        this.visiting_fee = in.readString();
        this.lng = in.readString();
        this.lat = in.readString();
        this.high_praise = in.readString();
        this.star = in.readInt();
        this.shop_name = in.readString();
        this.photo = in.readString();
        this.image = in.readString();
        this.unit = in.readString();
        this.has_specification = in.readInt();
        this.specification = in.createTypedArrayList(ServeStandard.CREATOR);
    }

    public static final Creator<ServeBean> CREATOR = new Creator<ServeBean>() {
        @Override
        public ServeBean createFromParcel(Parcel source) {
            return new ServeBean(source);
        }

        @Override
        public ServeBean[] newArray(int size) {
            return new ServeBean[size];
        }
    };
}
