package com.wuyou.user.data.types;

import com.google.gson.annotations.Expose;

/**
 * Created by DELL on 2018/8/30.
 */

public class EosActivityRewards implements EosType.Packer {

    @Expose
    private String participant;
    @Expose
    private String topic;
    @Expose
    private TypeAsset rewards;
    @Expose
    private String memo;

    public EosActivityRewards(String participant, String topic, long rewards, String memo) {
        this.participant = participant;
        this.topic = topic;
        this.rewards = new TypeAsset(rewards);
        this.memo = memo;
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putString(participant);
        writer.putString(topic);
        writer.putLongLE(rewards.getAmount());
        writer.putString(memo);
    }

    public String getActionName() {
        return "actirewards";
    }

}
