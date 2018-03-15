package com.wuyou.user.mvp.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gnway.bangwoba.activity.ChatActivity;
import com.gnway.bangwoba.activity.Leaving_message;
import com.wuyou.user.R;
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

    @Override
    public void fetchData() {

    }

    @OnClick({R.id.help_chat, R.id.help_leave_msg})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.help_chat:
                intent.setClass(mCtx, ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.help_leave_msg:
                intent.setClass(mCtx, Leaving_message.class);
                startActivity(intent);
                break;
        }
    }
}
