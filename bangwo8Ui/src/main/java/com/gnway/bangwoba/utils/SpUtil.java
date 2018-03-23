package com.gnway.bangwoba.utils;


import android.content.SharedPreferences;

/**
 * Created by luzhan on 2017/10/16.
 */

public class SpUtil {
    private static SpUtil spUtil;
    private SpUtil() {
    }

    public static SpUtil getInstance(){
        if(spUtil == null){
            spUtil = new SpUtil();
        }
        return spUtil;
    }
}
