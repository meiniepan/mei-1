package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2018/3/6.
 */
public class CommunityBean implements Parcelable {
    public Long mid;
    public String community_id;
    public String address;
    public String name;
    public double lng;
    public double lat;

    public CommunityBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mid);
        dest.writeString(this.community_id);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
    }

    protected CommunityBean(Parcel in) {
        this.mid = (Long) in.readValue(Long.class.getClassLoader());
        this.community_id = in.readString();
        this.address = in.readString();
        this.name = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
    }

    public static final Creator<CommunityBean> CREATOR = new Creator<CommunityBean>() {
        @Override
        public CommunityBean createFromParcel(Parcel source) {
            return new CommunityBean(source);
        }

        @Override
        public CommunityBean[] newArray(int size) {
            return new CommunityBean[size];
        }
    };
}
