package com.gnway.bangwoba.bean;

/**
 * Created by luzhan on 2017/6/29.
 */

public class UpFileSend {

    public static final int SEND_SUCCESS = 1;
    public static final int SEND_FAILED = 2;

    public UpFileSend(int fileSendStatus, int listPosition, int dbPosition) {
        this.fileSendStatus = fileSendStatus;
        this.listPosition = listPosition;
        this.dbPosition = dbPosition;
    }

    public int getFileSendStatus() {
        return fileSendStatus;
    }

    public void setFileSendStatus(int fileSendStatus) {
        this.fileSendStatus = fileSendStatus;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getDbPosition() {
        return dbPosition;
    }

    public void setDbPosition(int dbPosition) {
        this.dbPosition = dbPosition;
    }

    private int fileSendStatus;
    private int listPosition;
    private int dbPosition;

}
