package com.wuyou.user.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockInfo {

    /**
     * timestamp : 2018-09-06T02:00:24.000
     * producer : eosio
     * confirmed : 0
     * previous : 000000e372f28c3a14a90c240c912f8da243c5e1b7b4d44b60359d431e3821ae
     * transaction_mroot : 0000000000000000000000000000000000000000000000000000000000000000
     * action_mroot : 25dfcf68e3aa84764f0b1dfa5d0c2228505739944957dee846c67217fc45ab05
     * schedule_version : 0
     * new_producers : null
     * header_extensions : []
     * producer_signature : SIG_K1_KaUGAYbmft1i2P4eNEqnmiuWYaSgtsAYspSfQhTC9eW3dhtCyGHSmim4LVkidtVD5qjFG4a4U9HiwTCfjyw1wDxZKSXFA1
     * transactions : []
     * block_extensions : []
     * id : 000000e4210f11aed46da5e32140f8c3d645115f6c3e34ace9ba6670bf6055de
     * block_num : 228
     * ref_block_prefix : 3819269588
     */

    public String timestamp;
    public String producer;
    public int confirmed;
    public String previous;
    public String transaction_mroot;
    public String action_mroot;
    public int schedule_version;
    public Object new_producers;
    public String producer_signature;
    public String id;
    public int block_num;
    public long ref_block_prefix;
    public List<?> header_extensions;
    public List<?> transactions;
    public List<?> block_extensions;


    /**
     * status : executed
     * cpu_usage_us : 1116
     * net_usage_words : 18
     * trx : {"id":"3a5d447c8374d199440284f01884a0d33918c6947e6c60a96fc9dd05daff58f4","signatures":["SIG_K1_JyuA9aSKuDSZdWhL6dstDuuPfpjLA7YNiyoBXTJUa6xBtD12f6RmWeAJXpZdKDQaAGgYzpBxkmZkSHctwofgE6B14giAPA"],"compression":"none","packed_context_free_data":"","context_free_data":[],"packed_trx":"75efaa5be5ca3c3178ae000000000100a6823403ea3055000000572d3ccdcd0100a6823403ea305500000000a8ed32323000a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d20323031383039323600","transaction":{"expiration":"2018-09-26T02:31:17","ref_block_num":51941,"ref_block_prefix":2927112508,"max_net_usage_words":0,"max_cpu_usage_ms":0,"delay_sec":0,"context_free_actions":[],"actions":[{"account":"eosio.token","name":"transfer","authorization":[{"actor":"eosio.token","permission":"active"}],"data":{"from":"eosio.token","to":"votevotevote","quantity":"1000.0000 EOS","memo":"By Sam 20180926"},"hex_data":"00a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d203230313830393236"}],"transaction_extensions":[]}}
     */

    public String status;
    public int cpu_usage_us;
    public int net_usage_words;
    public TrxBean trx;

    public static class TrxBean {
        /**
         * id : 3a5d447c8374d199440284f01884a0d33918c6947e6c60a96fc9dd05daff58f4
         * signatures : ["SIG_K1_JyuA9aSKuDSZdWhL6dstDuuPfpjLA7YNiyoBXTJUa6xBtD12f6RmWeAJXpZdKDQaAGgYzpBxkmZkSHctwofgE6B14giAPA"]
         * compression : none
         * packed_context_free_data :
         * context_free_data : []
         * packed_trx : 75efaa5be5ca3c3178ae000000000100a6823403ea3055000000572d3ccdcd0100a6823403ea305500000000a8ed32323000a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d20323031383039323600
         * transaction : {"expiration":"2018-09-26T02:31:17","ref_block_num":51941,"ref_block_prefix":2927112508,"max_net_usage_words":0,"max_cpu_usage_ms":0,"delay_sec":0,"context_free_actions":[],"actions":[{"account":"eosio.token","name":"transfer","authorization":[{"actor":"eosio.token","permission":"active"}],"data":{"from":"eosio.token","to":"votevotevote","quantity":"1000.0000 EOS","memo":"By Sam 20180926"},"hex_data":"00a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d203230313830393236"}],"transaction_extensions":[]}
         */

        @SerializedName("id")
        public String idX;
        public String compression;
        public String packed_context_free_data;
        public String packed_trx;
        public TransactionBean transaction;
        public List<String> signatures;
        public List<?> context_free_data;

        public static class TransactionBean {
            /**
             * expiration : 2018-09-26T02:31:17
             * ref_block_num : 51941
             * ref_block_prefix : 2927112508
             * max_net_usage_words : 0
             * max_cpu_usage_ms : 0
             * delay_sec : 0
             * context_free_actions : []
             * actions : [{"account":"eosio.token","name":"transfer","authorization":[{"actor":"eosio.token","permission":"active"}],"data":{"from":"eosio.token","to":"votevotevote","quantity":"1000.0000 EOS","memo":"By Sam 20180926"},"hex_data":"00a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d203230313830393236"}]
             * transaction_extensions : []
             */

            public String expiration;
            public int ref_block_num;
            @SerializedName("ref_block_prefix")
            public long ref_block_prefixX;
            public int max_net_usage_words;
            public int max_cpu_usage_ms;
            public int delay_sec;
            public List<?> context_free_actions;
            public List<ActionsBean> actions;
            public List<?> transaction_extensions;

            public static class ActionsBean {
                /**
                 * account : eosio.token
                 * name : transfer
                 * authorization : [{"actor":"eosio.token","permission":"active"}]
                 * data : {"from":"eosio.token","to":"votevotevote","quantity":"1000.0000 EOS","memo":"By Sam 20180926"}
                 * hex_data : 00a6823403ea3055a032dd2ad3ad32dd809698000000000004454f53000000000f42792053616d203230313830393236
                 */

                public String account;
                public String name;
                public DataBean data;
                public String hex_data;
                public List<AuthorizationBean> authorization;

                public static class DataBean {
                    /**
                     * from : eosio.token
                     * to : votevotevote
                     * quantity : 1000.0000 EOS
                     * memo : By Sam 20180926
                     */

                    public String from;
                    public String to;
                    public String quantity;
                    public String memo;
                }

                public static class AuthorizationBean {
                    /**
                     * actor : eosio.token
                     * permission : active
                     */

                    public String actor;
                    public String permission;
                }
            }
        }
    }
}
