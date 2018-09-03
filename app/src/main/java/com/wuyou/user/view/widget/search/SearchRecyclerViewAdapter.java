package com.wuyou.user.view.widget.search;

import com.gs.buluo.common.widget.SwipeMenuLayout;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.SearchHistoryBean;

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
        helper.getView(R.id.history_item_delete).setOnClickListener(v -> {
            remove(helper.getAdapterPosition());
            menuLayout.quickClose();
            CarefreeDaoSession.getInstance().deleteHistory(item);
        });
        helper.addOnClickListener(R.id.tv_history_item);
    }


    public void setHistoryIcon(int historyIcon) {
        this.historyIcon = historyIcon;
    }

    public void setHistoryTextColor(int historyTextColor) {
        this.historyTextColor = historyTextColor;
    }
}
