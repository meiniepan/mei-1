package com.wuyou.user.adapter;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.SignRecordBean;

import java.util.Date;

/**
 * Created by DELL on 2018/6/6.
 */

public class SignInRecordAdapter extends BaseQuickAdapter<SignRecordBean, BaseHolder> {
    public SignInRecordAdapter(int res) {
        super(res);
    }

    @Override
    protected void convert(BaseHolder helper, SignRecordBean signRecordBean) {
        helper.setText(R.id.item_sign_in_time, TribeDateUtils.dateFormat7(new Date(signRecordBean.created_at * 1000)));
    }
}
