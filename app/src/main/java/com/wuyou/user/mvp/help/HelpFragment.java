package com.wuyou.user.mvp.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.gnway.bangwoba.activity.Leaving_message;
import com.gnway.bangwoba.global.Variable;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.service.HelpChatService;
import com.wuyou.user.view.activity.HelpRobotActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HelpFragment extends BaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_help;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void showError(String message, int res) {

    }

    @OnClick({R.id.help_chat, R.id.help_leave_msg, R.id.help_dialog})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.help_chat:
                intent.setClass(mCtx, HelpRobotActivity.class);
                startActivity(intent);
                break;
            case R.id.help_leave_msg:
                intent.setClass(mCtx, Leaving_message.class);
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
