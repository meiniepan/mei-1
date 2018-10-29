package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/26.
 */

public class VolunteerProjectBean {


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
    public String service_time;
    public String address;
    public String enroll_time;
    public String detailfile;
    public List<PositionsBean> positions;

    public static class PositionsBean {
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

        public static class EnrolledBean {
            /**
             * volunteer : samkunnbanb2
             * registered : 0
             * rewards : 0
             */

            public String volunteer;
            public int registered;
            public int rewards;
        }
    }
}
