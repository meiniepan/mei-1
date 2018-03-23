package com.gnway.bangwoba.bean;


import com.gnway.bangwo8sdk.bean.ChatMessage;

/**
 * Created by luzhan on 2017/10/17.
 */

public class NewMsgCome {
    public NewMsgCome(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    private ChatMessage chatMessage;
}
