package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.mvp.wallet.ScoreAccountActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/10/26.
 */

public class TimeBankMineFragment extends BaseFragment {
    @BindView(R.id.time_blank_mine_head)
    ImageView timeBlankMineHead;
    @BindView(R.id.time_bank_mine_name)
    TextView tbMineName;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_time_bank_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
        GlideUtils.loadImage(mCtx, userInfo.getAvatar(), timeBlankMineHead, true);
        tbMineName.setText(userInfo.getName());
    }

    @OnClick({R.id.time_bank_mine_wallet, R.id.time_bank_mine_record})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.time_bank_mine_wallet:
                intent.setClass(mCtx, ScoreAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.time_bank_mine_record:
                intent.setClass(mCtx, TBVolunteerRecordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
