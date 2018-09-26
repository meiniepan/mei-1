package com.wuyou.user.adapter;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.ScoreRecordBean;

import java.util.Date;

/**
 * Created by DELL on 2018/6/4.
 */

public class ScoreRecordAdapter extends BaseQuickAdapter<ScoreRecordBean, BaseHolder> {
    private int flag;

    public ScoreRecordAdapter(int flag) {
        super(R.layout.item_score_record);
        this.flag = flag;
    }

    @Override
    protected void convert(BaseHolder helper, ScoreRecordBean bean) {
        helper.setText(R.id.item_score_record_title, (translateSource(bean.source)))
                .setText(R.id.item_score_record_point, bean.points.replaceAll("EOS", "").split("\\.")[0])
                .setText(R.id.item_score_record_point_flag, flag == 0 ? "+" : "-")
                .setText(R.id.item_score_record_time, TribeDateUtils.dateFormat7(new Date(bean.created_at * 1000)));
    }


    private String formatString(String dateStr) {
        String[] aStrings = dateStr.split(" ");
        // 5
        if (aStrings[1].equals("Jan")) {
            aStrings[1] = "01";
        }
        if (aStrings[1].equals("Feb")) {
            aStrings[1] = "02";
        }
        if (aStrings[1].equals("Mar")) {
            aStrings[1] = "03";
        }
        if (aStrings[1].equals("Apr")) {
            aStrings[1] = "04";
        }
        if (aStrings[1].equals("May")) {
            aStrings[1] = "05";
        }
        if (aStrings[1].equals("Jun")) {
            aStrings[1] = "06";
        }
        if (aStrings[1].equals("Jul")) {
            aStrings[1] = "07";
        }
        if (aStrings[1].equals("Aug")) {
            aStrings[1] = "08";
        }
        if (aStrings[1].equals("Sep")) {
            aStrings[1] = "09";
        }
        if (aStrings[1].equals("Oct")) {
            aStrings[1] = "10";
        }
        if (aStrings[1].equals("Nov")) {
            aStrings[1] = "11";
        }
        if (aStrings[1].equals("Dec")) {
            aStrings[1] = "12";
        }
        return aStrings[5] + "-" + aStrings[1] + "-" + aStrings[2] + " " + aStrings[3];
    }


    private int translateSource(String source) {
        if ("dailyrewards".equals(source)) {
            return R.string.app_sign;
        } else if ("actirewards".equals(source)) {
            return R.string.activity;
        } else {
            return R.string.system_rewards;
        }
    }
}
