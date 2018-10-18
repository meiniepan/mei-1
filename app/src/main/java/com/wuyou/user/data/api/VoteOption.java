package com.wuyou.user.data.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/10/11.
 */

public class VoteOption implements Serializable{
    public List<Integer> option;

    public VoteOption(List<Integer> ints) {
        option = ints;
    }

}
