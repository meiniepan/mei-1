package com.wuyou.user.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.gnway.bangwoba.activity.Leaving_message;
import com.wuyou.user.R;
import com.wuyou.user.util.CommonUtil;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HelpActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_help;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.main_help);
    }

    @Override
    public void showError(String message, int res) {

    }

    @OnClick({R.id.help_chat, R.id.help_leave_msg, R.id.help_dialog})
    public void onViewClicked(View view) {
        if (CommonUtil.checkNetworkNoConnected(getCtx())) return;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.help_chat:
                intent.setClass(getCtx(), HelpRobotActivity.class);
                startActivity(intent);
                break;
            case R.id.help_leave_msg:
                intent.setClass(getCtx(), Leaving_message.class);
                startActivity(intent);
                break;
            case R.id.help_dialog:
                Intent dialog = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.help_phone)));
                dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialog);
                break;
        }
    }
}
