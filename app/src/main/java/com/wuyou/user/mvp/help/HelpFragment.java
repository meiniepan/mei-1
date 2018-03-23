package com.wuyou.user.mvp.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.gnway.bangwoba.activity.Leaving_message;
import com.gnway.bangwoba.global.Variable;
import com.wuyou.user.R;
import com.wuyou.user.service.HelpChatService;
import com.wuyou.user.view.activity.HelpRobotActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class HelpFragment extends BaseFragment {

    private Intent serviceIntent;

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

    @Override
    public void fetchData() {
        initHelpService();
        startService();
    }

    private void initHelpService() {
//        Variable.AgentId = aid.getText().toString();
//        String s = name.getText().toString().toLowerCase();
//        String substring = s.substring( Variable.AgentId.length());
        Variable.AgentId = "139971";
        String s = "123456789789";
        String substring = s.substring(Variable.AgentId.length());
        Variable.loginUser = "u4_" + Variable.AgentId + substring;
    }

    private void startService() {
        serviceIntent = new Intent(mCtx, HelpChatService.class);
        mCtx.startService(serviceIntent);
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
                Intent dialog = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "123456789"));
                dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialog);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) return;
    }
}
