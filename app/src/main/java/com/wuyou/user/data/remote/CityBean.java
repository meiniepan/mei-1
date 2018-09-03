package com.wuyou.user.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/8.
 */

public class CityBean implements Parcelable {
    public String city_id;
    public String city_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city_id);
        dest.writeString(this.city_name);
    }

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.city_id = in.readString();
        this.city_name = in.readString();
    }

    public static final Parcelable.Creator<CityBean> CREATOR = new Parcelable.Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };
}
