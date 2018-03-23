package com.gnway.bangwoba.bean;

import com.gnway.bangwo8sdk.Bangwo8SdkManager;
import com.gnway.bangwo8sdk.bean.ChatMessage;
import com.gnway.bangwo8sdk.listener.Bangwo8FileSendListener;
import com.gnway.bangwo8sdk.listener.Bangwo8IsFinishServiceListener;
import com.gnway.bangwo8sdk.listener.Bangwo8MessageListener;
import com.gnway.bangwo8sdk.listener.Bangwo8ResponseListener;

/**
 * Created by luzhan on 2017/9/28.
 */

public class VisitorEnd {
    public static final int NORMAL_LEAVE = 1;
    public static final int EXCEPTION_LEAVE = 2;

    public String getChatWithFirstJid() {
        return chatWithFirstJid;
    }

    public void setChatWithFirstJid(String chatWithFirstJid) {
        this.chatWithFirstJid = chatWithFirstJid;
    }

    private String chatWithFirstJid;

    public VisitorEnd(String chatWithFirstJid, int endType) {
        this.chatWithFirstJid = chatWithFirstJid;
        this.endType = endType;
    }

    public int getEndType() {
        return endType;
    }

    public void setEndType(int endType) {
        this.endType = endType;
    }

    private int endType;
}
