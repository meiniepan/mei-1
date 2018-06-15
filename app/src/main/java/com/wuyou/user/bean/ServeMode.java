package com.wuyou.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/6/15.
 */

public class ServeMode implements Parcelable {
    public String id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public ServeMode() {
    }

    protected ServeMode(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ServeMode> CREATOR = new Parcelable.Creator<ServeMode>() {
        @Override
        public ServeMode createFromParcel(Parcel source) {
            return new ServeMode(source);
        }

        @Override
        public ServeMode[] newArray(int size) {
            return new ServeMode[size];
        }
    };
}
