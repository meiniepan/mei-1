package com.wuyou.user.data.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by DELL on 2018/10/26.
 */

public class VolunteerProjectBean implements Parcelable{


    /**
     * id : 0
     * creator : samkunnbanb1
     * name : task8
     * service_time : 2019-12-21T10:00:00.000
     * address : address xxx
     * enroll_time : 2019-11-23T10:00:00.000
     * detailfile : file ipfs
     * positions : [{"name":"position_name_1","amount":8,"rewards":"10.0000 EOS","enrolled":[{"volunteer":"samkunnbanb2","registered":0,"rewards":0},{"volunteer":"samkunnbanb2","registered":1,"rewards":0}]}]
     */

    public int id;
    public String creator;
    public String name;
    public long service_time;
    public long service_end_time;
    public String address;
    public long enroll_time;
    public long enroll_end_time;
    public String detailfile;
    public String logofile;
    public List<PositionsBean> positions;

    public static class PositionsBean implements Parcelable{
        /**
         * name : position_name_1
         * amount : 8
         * rewards : 10.0000 EOS
         * enrolled : [{"volunteer":"samkunnbanb2","registered":0,"rewards":0},{"volunteer":"samkunnbanb2","registered":1,"rewards":0}]
         */

        public String name;
        public int amount;
        public String rewards;
        public List<EnrolledBean> enrolled;

        public static class EnrolledBean implements Parcelable{
            /**
             * volunteer : samkunnbanb2
             * registered : 0
             * rewards : 0
             */

            public String volunteer;
            public int registered;
            public int rewards;

            protected EnrolledBean(Parcel in) {
                volunteer = in.readString();
                registered = in.readInt();
                rewards = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(volunteer);
                dest.writeInt(registered);
                dest.writeInt(rewards);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<EnrolledBean> CREATOR = new Creator<EnrolledBean>() {
                @Override
                public EnrolledBean createFromParcel(Parcel in) {
                    return new EnrolledBean(in);
                }

                @Override
                public EnrolledBean[] newArray(int size) {
                    return new EnrolledBean[size];
                }
            };
        }

        protected PositionsBean(Parcel in) {
            name = in.readString();
            amount = in.readInt();
            rewards = in.readString();
            enrolled = in.createTypedArrayList(EnrolledBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeInt(amount);
            dest.writeString(rewards);
            dest.writeTypedList(enrolled);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PositionsBean> CREATOR = new Creator<PositionsBean>() {
            @Override
            public PositionsBean createFromParcel(Parcel in) {
                return new PositionsBean(in);
            }

            @Override
            public PositionsBean[] newArray(int size) {
                return new PositionsBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.creator);
        dest.writeString(this.name);
        dest.writeLong(this.service_time);
        dest.writeLong(this.service_end_time);
        dest.writeString(this.address);
        dest.writeLong(this.enroll_time);
        dest.writeLong(this.enroll_end_time);
        dest.writeString(this.detailfile);
        dest.writeString(this.logofile);
        dest.writeTypedList(this.positions);
    }

    public VolunteerProjectBean() {
    }

    protected VolunteerProjectBean(Parcel in) {
        this.id = in.readInt();
        this.creator = in.readString();
        this.name = in.readString();
        this.service_time = in.readLong();
        this.service_end_time = in.readLong();
        this.address = in.readString();
        this.enroll_time = in.readLong();
        this.enroll_end_time = in.readLong();
        this.detailfile = in.readString();
        this.logofile = in.readString();
        this.positions = in.createTypedArrayList(PositionsBean.CREATOR);
    }

    public static final Creator<VolunteerProjectBean> CREATOR = new Creator<VolunteerProjectBean>() {
        @Override
        public VolunteerProjectBean createFromParcel(Parcel source) {
            return new VolunteerProjectBean(source);
        }

        @Override
        public VolunteerProjectBean[] newArray(int size) {
            return new VolunteerProjectBean[size];
        }
    };
}
