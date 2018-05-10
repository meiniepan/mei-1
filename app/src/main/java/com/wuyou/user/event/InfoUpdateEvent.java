package com.wuyou.user.event;

/**
 * Created by DELL on 2018/5/10.
 */

public class InfoUpdateEvent {
    private String name;
    private String phone;

    public InfoUpdateEvent(String value) {
        name = value;
    }

    public InfoUpdateEvent(String phone, int type) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
