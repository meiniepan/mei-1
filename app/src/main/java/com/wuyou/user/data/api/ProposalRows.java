package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/24.
 */

public class ProposalRows {

    /**
     * rows : [{"proposal_name":"ayiuivl52fnq","packed_transaction":"68ebcf5b78e743e9ffc1000000000100a6823403ea3055000000572d3ccdcd0260e712256ea79d3700000000a8ed3232200d96064b83a5c100000000a8ed323227200d96064b83a5c160e712256ea79d37a08601000000000004454f53000000000642792053616d00"},{"proposal_name":"mukang123123","packed_transaction":"a5eacf5bf8e5aec3908b000000000100a6823403ea3055000000572d3ccdcd0230441822b069a09600000000a8ed3232200d96064b83a5c100000000a8ed323227200d96064b83a5c130441822b069a096a08601000000000004454f53000000000642792053616d00"}]
     * more : false
     */

    public boolean more;
    public List<RowsBean> rows;

    public static class RowsBean {
        /**
         * proposal_name : ayiuivl52fnq
         * packed_transaction : 68ebcf5b78e743e9ffc1000000000100a6823403ea3055000000572d3ccdcd0260e712256ea79d3700000000a8ed3232200d96064b83a5c100000000a8ed323227200d96064b83a5c160e712256ea79d37a08601000000000004454f53000000000642792053616d00
         */

        public String proposal_name;
        public String packed_transaction;
    }
}
