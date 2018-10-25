package com.wuyou.user.data.api;

/**
 * Created by DELL on 2018/10/24.
 */

public class ApproveBean {

    public String proposer;
    public String proposal_name;
    public PermissionLevel level;

    public ApproveBean(String name, String proposal_name) {
        proposer = name;
        this.proposal_name = proposal_name;
        level = new PermissionLevel(proposal_name, "active");
    }

    public String getActionName() {
        return "approve";
    }

    class PermissionLevel {
        public String actor;
        public String permission;

        public PermissionLevel(String name, String permission) {
            this.actor = name;
            this.permission = permission;
        }
    }
}
