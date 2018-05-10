package com.wuyou.user.view.widget.search;

import android.view.View;

import com.gs.buluo.common.widget.SwipeMenuLayout;
import com.wuyou.user.CarefreeDaoSession;
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
        SwipeMenuLayout menuLayout = helper.getView(R.id.history_item_swipe);
        helper.getView(R.id.history_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(helper.getAdapterPosition());
                menuLayout.quickClose();
                CarefreeDaoSession.getInstance().deleteHistory(item);
            }
        });
    }

    public void setHistoryIcon(int historyIcon) {
        this.historyIcon = historyIcon;
    }

    public void setHistoryTextColor(int historyTextColor) {
        this.historyTextColor = historyTextColor;
    }
}
