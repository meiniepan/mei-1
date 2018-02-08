package com.wuyou.user.mvp.home;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class MainServeChildrenAdapter extends BaseQuickAdapter<CategoryChild,BaseHolder> {

    public MainServeChildrenAdapter(int layoutResId, @Nullable List<CategoryChild> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, CategoryChild item) {
        TextView textView = helper.getView(R.id.main_serve_name);
        textView.setText(item.name);
    }


}
