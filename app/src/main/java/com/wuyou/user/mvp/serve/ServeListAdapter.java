package com.wuyou.user.mvp.serve;

import android.support.v7.widget.RecyclerView;

import com.wuyou.user.bean.ServeBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

/**
 * Created by hjn on 2018/2/6.
 */

class ServeListAdapter extends BaseQuickAdapter<ServeBean,BaseHolder> {
    public ServeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder helper, ServeBean item) {

    }
}
