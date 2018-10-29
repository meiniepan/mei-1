package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/29.
 */

public class VolunteerRecordBean {
    public List<Participated> enrolled;

    public class Participated {
        public String organizer;
        public int id;
        public int registered;
        public int rewards;
    }
}
