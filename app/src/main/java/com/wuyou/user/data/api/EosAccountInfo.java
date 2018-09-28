package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/9/10.
 */

public class EosAccountInfo {

    /**
     * account_name : houjingnan32
     * head_block_num : 722267
     * head_block_time : 2018-09-10T06:17:23.500
     * privileged : false
     * last_code_update : 1970-01-01T00:00:00.000
     * created : 2018-09-10T06:14:44.000
     * ram_quota : 136739
     * net_weight : 20000
     * cpu_weight : 20000
     * net_limit : {"used":0,"available":"10658466635294","max":"10658466635294"}
     * cpu_limit : {"used":0,"available":"2032941176470","max":"2032941176470"}
     * ram_usage : 3446
     * permissions : [{"perm_name":"active","parent":"owner","required_auth":{"threshold":1,"keys":[{"key":"EOS6eWTEiiUpydfaesgX8mpecuFT5V5AczAb2XSBA6kCAaQgXTRD6","weight":1}],"accounts":[],"waits":[]}},{"perm_name":"owner","parent":"","required_auth":{"threshold":1,"keys":[{"key":"EOS6eWTEiiUpydfaesgX8mpecuFT5V5AczAb2XSBA6kCAaQgXTRD6","weight":1}],"accounts":[],"waits":[]}}]
     * total_resources : {"owner":"houjingnan32","net_weight":"2.0000 EOS","cpu_weight":"2.0000 EOS","ram_bytes":136739}
     * self_delegated_bandwidth : {"from":"houjingnan32","to":"houjingnan32","net_weight":"2.0000 EOS","cpu_weight":"2.0000 EOS"}
     * refund_request : null
     * voter_info : {"owner":"houjingnan32","proxy":"","producers":[],"staked":40000,"last_vote_weight":"0.00000000000000000","proxied_vote_weight":"0.00000000000000000","is_proxy":0}
     */

    public String account_name;
    public String head_block_time;
    public boolean privileged;
    public String last_code_update;
    public String core_liquid_balance;
    public List<PermissionsBean> permissions;
    public String created;
    public long ram_usage;
    public TotalResource total_resources;
    public Limit net_limit;
    public Limit cpu_limit;

    /**
     * used : 0
     * available : 2032941176470
     * max : 2032941176470
     */


    public static class Limit {
        public long used;
        public long available;
        public long max;
    }

    /**
     * owner : houjingnan32
     * net_weight : 2.0000 EOS
     * cpu_weight : 2.0000 EOS
     * ram_bytes : 136739
     */

    public static class TotalResource {

        public String owner;
        public String net_weight;
        public String cpu_weight;
        public long ram_bytes;
    }

    public static class PermissionsBean {
        /**
         * perm_name : active
         * parent : owner
         * required_auth : {"threshold":1,"keys":[{"key":"EOS6eWTEiiUpydfaesgX8mpecuFT5V5AczAb2XSBA6kCAaQgXTRD6","weight":1}],"accounts":[],"waits":[]}
         */

        public String perm_name;
        public String parent;
        public RequiredAuthBean required_auth;

        public static class RequiredAuthBean {
            /**
             * threshold : 1
             * keys : [{"key":"EOS6eWTEiiUpydfaesgX8mpecuFT5V5AczAb2XSBA6kCAaQgXTRD6","weight":1}]
             * accounts : []
             * waits : []
             */

            public int threshold;
            public List<KeysBean> keys;
            public List<?> accounts;
            public List<?> waits;

            public static class KeysBean {
                /**
                 * key : EOS6eWTEiiUpydfaesgX8mpecuFT5V5AczAb2XSBA6kCAaQgXTRD6
                 * weight : 1
                 */

                public String key;
                public int weight;
            }
        }
    }
}
