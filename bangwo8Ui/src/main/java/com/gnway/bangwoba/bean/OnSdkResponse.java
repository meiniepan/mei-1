package com.gnway.bangwoba.bean;

/**
 * Created by luzhan on 2017/10/16.
 */

public class OnSdkResponse {

    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAILED = 2;


    public int getQueueLocation() {
        return queueLocation;
    }

    public void setQueueLocation(int queueLocation) {
        this.queueLocation = queueLocation;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    private int queueLocation;

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    private String failedReason;

    public OnSdkResponse(int queueLocation, String chatMessage, int messageType, String filePath, int requestResult,String failedReason) {
        this.queueLocation = queueLocation;
        this.chatMessage = chatMessage;
        this.messageType = messageType;
        this.filePath = filePath;
        this.requestResult = requestResult;
        this.failedReason = failedReason;
    }

    private String chatMessage;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    private int messageType;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;


    public int getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(int requestResult) {
        this.requestResult = requestResult;
    }

    private int requestResult;
}
