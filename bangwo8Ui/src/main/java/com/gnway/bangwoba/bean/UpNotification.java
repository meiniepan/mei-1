package com.gnway.bangwoba.bean;

import android.content.Context;

import com.gnway.bangwo8sdk.bean.ChatMessage;

/**
 * Created by luzhan on 2017/10/18.
 */

public class UpNotification {

    public UpNotification(Context context, ChatMessage chatMessage, int drawableId) {
        this.context = context;
        this.chatMessage = chatMessage;
        this.drawableId = drawableId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }


    private Context context;
    private ChatMessage chatMessage;
    private int drawableId;

}
