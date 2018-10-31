package com.wuyou.user.data.types;

import com.google.gson.annotations.Expose;

/**
 * Created by DELL on 2018/8/30.
 */

public class EosDailyRewards implements EosType.Packer{
    @Expose
    private String checker;
    @Expose
    private long localdate;
    @Expose
    private String memo;
    @Expose
    private TypeAsset rewards;

    public EosDailyRewards(String checker, long localdate, String memo, TypeAsset rewards) {
        this.checker = checker;
        this.localdate = localdate;
        this.memo = memo;
        this.rewards = rewards;
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putString(checker);
        writer.putLongLE(localdate);
        writer.putString(memo);
        writer.putLongLE(rewards.getAmount());
    }

    public String getActionName() {
        return "dailyrewards";
    }

}
