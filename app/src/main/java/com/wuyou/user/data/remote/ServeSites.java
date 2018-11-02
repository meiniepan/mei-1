package com.wuyou.user.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/12.
 */

public class ServeSites implements Parcelable {
    public String id;
    public String name;
    public String logo;
    public String provice_name;
    public String city_name;
    public String county_name;
    public String town_name;
    public String community_name;
    public String address;
    public String open_all_day;
    public String start_at;
    public String end_at;
    public Double lng;
    public Double lat;
    public float distance;
    public String introduce;

    public ServeSites(String name, String address, double lat, double lon) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lon;
    }

    public ServeSites() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeString(this.provice_name);
        dest.writeString(this.city_name);
        dest.writeString(this.county_name);
        dest.writeString(this.town_name);
        dest.writeString(this.community_name);
        dest.writeString(this.address);
        dest.writeString(this.open_all_day);
        dest.writeString(this.start_at);
        dest.writeString(this.end_at);
        dest.writeValue(this.lng);
        dest.writeValue(this.lat);
        dest.writeFloat(this.distance);
        dest.writeString(this.introduce);
    }

    protected ServeSites(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
        this.provice_name = in.readString();
        this.city_name = in.readString();
        this.county_name = in.readString();
        this.town_name = in.readString();
        this.community_name = in.readString();
        this.address = in.readString();
        this.open_all_day = in.readString();
        this.start_at = in.readString();
        this.end_at = in.readString();
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.distance = in.readFloat();
        this.introduce = in.readString();
    }

    public static final Parcelable.Creator<ServeSites> CREATOR = new Parcelable.Creator<ServeSites>() {
        @Override
        public ServeSites createFromParcel(Parcel source) {
            return new ServeSites(source);
        }

        @Override
        public ServeSites[] newArray(int size) {
            return new ServeSites[size];
        }
    };
}
