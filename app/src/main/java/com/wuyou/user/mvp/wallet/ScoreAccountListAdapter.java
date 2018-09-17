package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.EosAccount;

import java.util.List;

/**
 * Created by Solang on 2018/9/12.
 */

public class ScoreAccountListAdapter extends BaseQuickAdapter<EosAccount, BaseHolder> {
    List<EosAccount> data;

    public ScoreAccountListAdapter(int layoutResId, @Nullable List<EosAccount> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseHolder helper, EosAccount item) {
        if (helper.getAdapterPosition() % 3 == 1) {
            helper.getView(R.id.avatar_bac).setBackgroundResource(R.drawable.account_avatar_bac_2);
        } else if (helper.getAdapterPosition() % 3 == 2) {
            helper.getView(R.id.avatar_bac).setBackgroundResource(R.drawable.account_avatar_bac_3);
        } else {
            helper.getView(R.id.avatar_bac).setBackgroundResource(R.drawable.account_avatar_bac_1);
        }
        helper.setText(R.id.tv_account_name_1, item.getName());
        CheckBox checkBox = helper.getView(R.id.cb_main_account);
        checkBox.setChecked(item.getMain());
        checkBox.setOnClickListener(v -> {
            if (item.getMain()) {
                checkBox.setChecked(item.getMain());
                return;
            }
            for (int i = 0; i < data.size(); i++) {
                if (i == helper.getAdapterPosition()) {
                    CarefreeDaoSession.getInstance().setMainAccount(data.get(i));
                }
            }
            notifyDataSetChanged();
        });
        helper.getView(R.id.manager_account_layout).setOnClickListener(v -> {
            CarefreeDaoSession.getInstance().setMainAccount(item.getName());
            Intent intent = new Intent(mContext, AccountInfoActivity.class);
            intent.putExtra(Constant.IMPORT_ACCOUNT, item.getName());
            mContext.startActivity(intent);
            AppManager.getAppManager().finishActivity(ManagerAccountActivity.class);
        });
    }
}