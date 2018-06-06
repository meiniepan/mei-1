package com.wuyou.user.adapter;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.bean.ScoreRecordBean;

import java.util.Date;

/**
 * Created by DELL on 2018/6/4.
 */

public class ScoreRecordAdapter extends BaseQuickAdapter<ScoreRecordBean, BaseHolder> {
    private int flag;

    public ScoreRecordAdapter(int layoutResId, int flag) {
        super(layoutResId);
        this.flag = flag;
    }

    @Override
    protected void convert(BaseHolder helper, ScoreRecordBean bean) {
        helper.setText(R.id.item_score_record_title, (bean.source))
                .setText(R.id.item_score_record_point, bean.points)
                .setText(R.id.item_score_record_point_flag, flag == 0 ? "+" : "-")
                .setText(R.id.item_score_record_time, TribeDateUtils.dateFormat(new Date(bean.created_at * 1000)));
    }
}
