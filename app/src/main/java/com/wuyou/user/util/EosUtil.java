package com.wuyou.user.util;

import android.util.Log;

import com.gs.buluo.common.utils.TribeDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by DELL on 2018/10/11.
 */

public class EosUtil {
        private static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String formatTimePoint(long timemilis) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(timemilis));
    }

    public static String UTCToCST(String UTCStr) {
        Calendar calendar = null;
        try {
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            date = sdf.parse(UTCStr);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            return TribeDateUtils.dateFormat(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
