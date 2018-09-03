package com.wuyou.user.mvp.companies;

import android.support.annotation.Nullable;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.data.local.db.UserInfo;

import java.util.List;

/**
 * Created by hjn on 2018\1\26 0026.
 */

public class CompaniesAdapter extends BaseQuickAdapter<UserInfo, BaseHolder> {

    public CompaniesAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, UserInfo item) {

    }
}