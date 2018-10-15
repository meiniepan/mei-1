package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/15.
 */

public class VoteRecord {

    /**
     * rows : [{"id":0,"voteid":0,"answeritem":[{"option":[1]}]}]
     * more : false
     */

    public boolean more;
    public List<RowsBean> rows;

    public static class RowsBean {
        /**
         * id : 0
         * voteid : 0
         * answeritem : [{"option":[1]}]
         */

        public String id;
        public String voteid;
        public List<AnsweritemBean> answeritem;

        public static class AnsweritemBean {
            public List<Integer> option;
        }
    }
}
