package com.wuyou.user.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseSubscriber;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.EosAccountInfo;
import com.wuyou.user.mvp.score.ScoreActivity;
import com.wuyou.user.mvp.score.ScoreRecordActivity;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/9/11.
 */

public class ScoreAccountActivity extends BaseActivity {
    @BindView(R.id.iv_account_avatar)
    ImageView ivAccountAvatar;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.tv_account_score)
    TextView tvAccountScore;
    @BindView(R.id.ll_backup_pk)
    LinearLayout llBackupPk;
    @BindView(R.id.tv_obtain)
    TextView tvObtain;
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.drawerL)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_above)
    LinearLayout layout;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initDrawerLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = CarefreeDaoSession.getInstance().getMainAccount().getName();
        tvAccountName.setText(name);
        getAccountScore(name);
    }

    public void getAccountScore(String name) {
        EoscDataManager.getIns().readAccountInfo(name).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<EosAccountInfo>() {
                    @Override
                    public void onSuccess(EosAccountInfo eosAccountInfo) {
                        scoreAmount = eosAccountInfo.core_liquid_balance.replace("EOS", "");
                        tvAccountScore.setText(scoreAmount);
                    }
                });
    }

    private void initDrawerLayout() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        WindowManager wm = this.getWindowManager();//获取屏幕宽高
        int width1 = wm.getDefaultDisplay().getWidth();
        int height1 = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams para = layout.getLayoutParams();//获取drawerlayout的布局
        para.width = width1 * 4 / 7;//修改宽度
        para.height = height1;//修改高度
        layout.setLayoutParams(para); //设置修改后的布局。
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_account;
    }

    private String scoreAmount;

    @OnClick({R.id.iv_more, R.id.ll_backup_pk, R.id.tv_exchange, R.id.back_1, R.id.back_2, R.id.ll_import, R.id.ll_manager, R.id.ll_score, R.id.score_obtain_layout})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_more:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.ll_backup_pk:
                intent.setClass(getCtx(), BackupActivity.class);
                intent.putExtra(Constant.SCORE_AMOUNT, scoreAmount);
                startActivity(intent);
                break;
            case R.id.tv_exchange:
                break;
            case R.id.back_1:
                finish();
                break;
            case R.id.back_2:
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.ll_import:
                intent.setClass(getCtx(), ImportAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_manager:
                intent.setClass(getCtx(), ManagerAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_score:
                intent.setClass(getCtx(), ScoreRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.score_obtain_layout:
                intent.setClass(getCtx(), ScoreActivity.class);
                startActivity(intent);
                break;
        }
    }

}
