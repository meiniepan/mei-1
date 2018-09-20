package com.wuyou.user.data.types;

/**
 * Created by DELL on 2018/8/30.
 */

public class EosActivityRewards implements EosType.Packer {

    private String participant;

    private String topic;

    private TypeAsset rewards;

    private String memo;

    public EosActivityRewards(String participant, String topic, String rewards, String memo) {
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
