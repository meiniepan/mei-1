package com.wuyou.user.view.widget.search;

import android.widget.ImageView;

import com.wuyou.user.R;
import com.wuyou.user.bean.SearchHistoryBean;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by limuyang on 2017/7/29.
 */

public class SearchRecyclerViewAdapter extends BaseQuickAdapter<SearchHistoryBean, BaseHolder> {
    private int historyIcon;

    private int historyTextColor;

    public SearchRecyclerViewAdapter(List<SearchHistoryBean> list) {
        super(R.layout.view_rv_item, list);
    }

    @Override
    protected void convert(BaseHolder helper, SearchHistoryBean item) {
        helper.setText(R.id.tv_history_item, item.getTitle());
    }

    public void setHistoryIcon(int historyIcon) {
        this.historyIcon = historyIcon;
    }

    public void setHistoryTextColor(int historyTextColor) {
        this.historyTextColor = historyTextColor;
    }
}
