package com.wuyou.user.mvp.home;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class MainServeChildrenAdapter extends BaseQuickAdapter<CategoryChild, BaseHolder> {

    public MainServeChildrenAdapter(int layoutResId, @Nullable List<CategoryChild> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, CategoryChild item) {
        TextView textView = helper.getView(R.id.main_serve_name);
        textView.setText(item.name);
        int padding = textView.getPaddingStart();
        if (item.position % 3 == 0) {
            textView.setBackgroundResource(R.drawable.orange_border);
        } else if (item.position % 3 == 1) {
            textView.setBackgroundResource(R.drawable.red_border);
        } else {
            textView.setBackgroundResource(R.drawable.night_blue_border);
        }
        textView.setPadding(padding, padding, padding, padding);
    }
}
