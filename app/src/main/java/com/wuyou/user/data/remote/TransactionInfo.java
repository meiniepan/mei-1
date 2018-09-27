package com.wuyou.user.data.remote;

import java.util.List;

/**
 * Created by DELL on 2018/9/26.
 */

public class TransactionInfo {

    /**
     * _id : 5ba9b5d333a882a9ee5ef9e9
     * trx_id : 000015d8101046265bb0037e9eba11e1390cfcb88d524908b7c5730e29675dee
     * accepted : true
     * actions : [{"account":"eosio","name":"onblock","authorization":[{"actor":"eosio","permission":"active"}],"data":"02f248460000000000ea305500000002657550f2ccdd41f69aa583f5748c44f69db51a135ecdff022296a5c154c400000000000000000000000000000000000000000000000000000000000000002273d53679139e73a043812c848c45d442d913951df7eed43757381ce4769c21000000000000"}]
     * context_free_actions : []
     * context_free_data : []
     * createdAt : 2018-09-25T04:13:07.798Z
     * delay_sec : 0
     * expiration : 2018-09-06T23:47:14
     * implicit : true
     * max_cpu_usage_ms : 0
     * max_net_usage_words : 0
     * ref_block_num : 25974
     * ref_block_prefix : 530429591
     * scheduled : false
     * signatures : []
     * transaction_extensions : []
     */

    public String _id;
    public String trx_id;
    public boolean accepted;
    public String createdAt;
    public int delay_sec;
    public String expiration;
    public boolean implicit;
    public int max_cpu_usage_ms;
    public int max_net_usage_words;
    public int ref_block_num;
    public int ref_block_prefix;
    public boolean scheduled;
    public List<ActionsBean> actions;
    public List<?> context_free_actions;
    public List<?> context_free_data;
    public List<?> signatures;
    public List<?> transaction_extensions;

    public static class ActionsBean {
        /**
         * account : eosio
         * name : onblock
         * authorization : [{"actor":"eosio","permission":"active"}]
         * data : 02f248460000000000ea305500000002657550f2ccdd41f69aa583f5748c44f69db51a135ecdff022296a5c154c400000000000000000000000000000000000000000000000000000000000000002273d53679139e73a043812c848c45d442d913951df7eed43757381ce4769c21000000000000
         */

        public String account;
        public String name;
        public String data;
        public List<AuthorizationBean> authorization;

        public static class AuthorizationBean {
            /**
             * actor : eosio
             * permission : active
             */

            public String actor;
            public String permission;
        }
    }
}
