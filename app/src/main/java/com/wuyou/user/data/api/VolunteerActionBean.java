package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/29.
 */

public class VolunteerActionBean {

    public String id;
    public String volunteer;
    public String organizer;
    public String name;
    public String position_name;

    public VolunteerActionBean(String accountName, String id, String organizer, String projectName) {
        this.name = projectName;
        volunteer = accountName;
        this.id = id;
        this.organizer = organizer;
        this.position_name = position_name;
    }
    public VolunteerActionBean(String accountName, String id, String organizer, String projectName, String position_name) {
        this.name = projectName;
        volunteer = accountName;
        this.id = id;
        this.organizer = organizer;
        this.position_name = position_name;
    }


    public String getRegisterAction() {
        return "registration";
    }

    public String getParticipateAction() {
        return "participate";
    }

    public String getRewardsAction(){
        return "rewards";
    }
}

