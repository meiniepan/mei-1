package com.wuyou.user.data.types;

import com.google.gson.annotations.Expose;

/**
 * Created by DELL on 2018/8/30.
 */

public class EosActivityRewards implements EosType.Packer{
    @Expose
    private String participant;
    @Expose
    private String id;

    public EosActivityRewards(String participant, String id) {
        this.participant = participant;
        this.id = id;
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putString(participant);
        writer.putString(id);
    }

    public String getActionName() {
        return "dailyrewards";
    }

}
