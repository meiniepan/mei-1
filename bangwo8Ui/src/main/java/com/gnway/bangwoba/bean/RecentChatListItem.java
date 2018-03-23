package com.gnway.bangwoba.bean;

/**
 * Created by luzhan on 2017/9/14.
 */

public class RecentChatListItem {
    public static final int SHOW_UNREAD = 1;
    public static final int HIDE_UNREAD = 2;
    public static final int FINISH_SERVICE = 3;
    public static final int NOT_FINISH_SERVICE = 4;
    public static final int MESSAGE_SEND = 5;
    public static final int MESSAGE_RECEIVE = 6;
    public static final int SINGLE_CHAT = 7;
    public static final int GROUP_CHAT = 8;
    public static final int NOTICE = 9;

    public String getChatWithFirstJid() {
        return chatWithFirstJid;
    }

    public void setChatWithFirstJid(String chatWithFirstJid) {
        this.chatWithFirstJid = chatWithFirstJid;
    }

    private String chatWithFirstJid;

    public int getToOrFromType() {
        return toOrFromType;
    }

    public void setToOrFromType(int toOrFromType) {
        this.toOrFromType = toOrFromType;
    }

    private int toOrFromType;


    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public int getUnReadNumber() {
        return unReadNumber;
    }

    public void setUnReadNumber(int unReadNumber) {
        this.unReadNumber = unReadNumber;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getShowOrHideUnRead() {
        return showOrHideUnRead;
    }

    public void setShowOrHideUnRead(int showOrHideUnRead) {
        this.showOrHideUnRead = showOrHideUnRead;
    }

    private String chatMessage;
    private long messageTime;
    private int unReadNumber;
    private int isFinish;
    private int showOrHideUnRead;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    private int chatType;

    public String getRoomJid() {
        return roomJid;
    }

    public void setRoomJid(String roomJid) {
        this.roomJid = roomJid;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    private String roomJid;
    private String chatId;
    private String ipInfo;

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    private String avatarPath;
}
