package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/13.
 */

public class ServeStandard implements Parcelable, Cloneable {
    public String id;
    public String name;
    public float price;
    public int stock;
    public String photo;

    public ServeStandard() {
    }

    @Override
    public ServeStandard clone() {
        try {
            return (ServeStandard) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ServeStandard();
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
    }

    protected ServeStandard(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readFloat();
        this.stock = in.readInt();
        this.photo = in.readString();
    }

    public static final Creator<ServeStandard> CREATOR = new Creator<ServeStandard>() {
        @Override
        public ServeStandard createFromParcel(Parcel source) {
            return new ServeStandard(source);
        }

        @Override
        public ServeStandard[] newArray(int size) {
            return new ServeStandard[size];
        }
    };
}
