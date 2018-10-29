package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/29.
 */

public class VolunteerRegisterBean {

    public String id;
    public String volunteer;
    public String organizer;
    public String name;

    public VolunteerRegisterBean(String accountName, String id, String organizer, String projectName) {
        this.name = projectName;
        volunteer = accountName;
        this.id = id;
        this.organizer = organizer;
    }


    public String getReisterAction() {
        return "registration";
    }
}

