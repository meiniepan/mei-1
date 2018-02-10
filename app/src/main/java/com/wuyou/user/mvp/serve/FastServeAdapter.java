package com.wuyou.user.mvp.serve;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import com.wuyou.user.R;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/10.
 */

class FastServeAdapter extends BaseQuickAdapter<ServeDetailBean, BaseHolder> {
    public FastServeAdapter(int layoutResId, @Nullable List<ServeDetailBean> data) {
        super(layoutResId, data);
    }

    private int checkPos = 0;

    @Override
    protected void convert(BaseHolder helper, ServeDetailBean item) {
        View view = helper.getView(R.id.fast_bg);
        if (item.level.contains("钻石")) {
            view.setBackgroundResource(R.mipmap.serve_high);
        } else if (item.level.contains("优享")) {
            view.setBackgroundResource(R.mipmap.serve_middle);
        } else {
            view.setBackgroundResource(R.mipmap.serve_low);
        }
        helper.setText(R.id.fast_title, item.level)
                .setText(R.id.fast_price, item.price);


        RadioButton radioButton = helper.getView(R.id.fast_radio);
        if (helper.getAdapterPosition() == checkPos) {
            radioButton.setChecked(true);
            item.selected = true;
        } else {
            radioButton.setChecked(false);
            item.selected = false;
        }
    }

    public void setCheckPos(int pos) {
        checkPos = pos;
        notifyDataSetChanged();
    }
}
