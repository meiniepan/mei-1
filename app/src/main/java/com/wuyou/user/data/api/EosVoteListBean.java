package com.wuyou.user.data.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by DELL on 2018/10/15.
 */

public class EosVoteListBean {

    /**
     * rows : [{"id":0,"creator":"ayiuivl52fnq","title":"问卷test","logo":"QmWuW8X5KVTKjg7LHGVqLCJGS3VHquxNc9QAAqBaPxST6x","description":"desc","organization":"org","contents":[{"question":"hello?","option":[{"optioncontent":"yes","number":0},{"optioncontent":"what","number":2}],"single":0}],"voters":["ayiuivl52fnq","jfiqco1l4cax"],"end_time":"2018-10-13T02:48:06.000"},{"id":1,"creator":"jfiqco1l4cax","title":"merchant 问卷test","logo":"QmWuW8X5KVTKjg7LHGVqLCJGS3VHquxNc9QAAqBaPxST6x","description":"desc","organization":"org","contents":[{"question":"你是 zz 么","option":[{"optioncontent":"yes","number":0},{"optioncontent":"what","number":0}],"single":0}],"voters":[],"end_time":"2018-10-13T07:38:51.000"}]
     * more : false
     */

    public boolean more;
    public List<RowsBean> rows;

    public static class RowsBean implements Parcelable {
        /**
         * id : 0
         * creator : ayiuivl52fnq
         * title : 问卷test
         * logo : QmWuW8X5KVTKjg7LHGVqLCJGS3VHquxNc9QAAqBaPxST6x
         * description : desc
         * organization : org
         * contents : [{"question":"hello?","option":[{"optioncontent":"yes","number":0},{"optioncontent":"what","number":2}],"single":0}]
         * voters : ["ayiuivl52fnq","jfiqco1l4cax"]
         * end_time : 2018-10-13T02:48:06.000
         */

        public String id;
        public String creator;
        public String title;
        public String logo;
        public String description;
        public String organization;
        public String end_time;
        public List<VoteQuestion> contents;
        public List<String> voters;

        public RowsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.creator);
            dest.writeString(this.title);
            dest.writeString(this.logo);
            dest.writeString(this.description);
            dest.writeString(this.organization);
            dest.writeString(this.end_time);
            dest.writeTypedList(this.contents);
            dest.writeStringList(this.voters);
        }

        protected RowsBean(Parcel in) {
            this.id = in.readString();
            this.creator = in.readString();
            this.title = in.readString();
            this.logo = in.readString();
            this.description = in.readString();
            this.organization = in.readString();
            this.end_time = in.readString();
            this.contents = in.createTypedArrayList(VoteQuestion.CREATOR);
            this.voters = in.createStringArrayList();
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel source) {
                return new RowsBean(source);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };
    }
}
