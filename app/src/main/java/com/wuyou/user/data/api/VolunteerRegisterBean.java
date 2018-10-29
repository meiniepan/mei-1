package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/29.
 */

public class VolunteerRegisterBean {

    public String id;
    public String volunteer;
    public String organizer;
    public String name;
    public String position_name="position_name_1";

    public VolunteerRegisterBean(String accountName, String id, String organizer, String projectName) {
        this.name = projectName;
        volunteer = accountName;
        this.id = id;
        this.organizer = organizer;
    }


    public String getRegisterAction() {
        return "registration";
    }

    public String getRewardAction() {
        return "rewards";
    }
}

