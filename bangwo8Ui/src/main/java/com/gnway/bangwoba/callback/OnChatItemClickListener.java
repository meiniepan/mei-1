package com.gnway.bangwoba.callback;

import android.view.View;

/**
 * Created by luzhan on 2017/8/14.
 */

public interface OnChatItemClickListener {
    void onChatItemClick(int position,View view);
    void onChatItemLongClick(int position,View view);
}
