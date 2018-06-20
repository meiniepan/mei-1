package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/13.
 */

public class ServeSpecification implements Parcelable, Cloneable {
    public String id;
    public String name;
    public float price;
    public int stock;
    public String photo;
    public String sales;

    public ServeSpecification() {
    }

    @Override
    public ServeSpecification clone() {
        try {
            return (ServeSpecification) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ServeSpecification();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeInt(this.stock);
        dest.writeString(this.photo);
        dest.writeString(this.sales);
    }

    protected ServeSpecification(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readFloat();
        this.stock = in.readInt();
        this.photo = in.readString();
        this.sales = in.readString();
    }

    public static final Creator<ServeSpecification> CREATOR = new Creator<ServeSpecification>() {
        @Override
        public ServeSpecification createFromParcel(Parcel source) {
            return new ServeSpecification(source);
        }

        @Override
        public ServeSpecification[] newArray(int size) {
            return new ServeSpecification[size];
        }
    };
}
