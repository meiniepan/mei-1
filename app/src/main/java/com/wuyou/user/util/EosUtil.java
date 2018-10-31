package com.wuyou.user.util;

import android.util.Log;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.network.ipfs.ChainIPFS;
import com.wuyou.user.network.ipfs.ChainNamedStreamable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DELL on 2018/10/11.
 */

public class EosUtil {
    private static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";


    public static String formatZeroTimePoint(long timemilis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(timemilis));
    }

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

    public void uploadFileToIpfs(File des) throws IOException {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            ChainIPFS ipfs = new ChainIPFS(Constant.IPFS_URL.contains(Constant.BASE_CHAIN_URL) ? Constant.BASE_CHAIN_URL : Constant.DEV_BASE_CHAIN_URL, 5001);
            ipfs.local();
            ChainNamedStreamable.FileWrapper file = new ChainNamedStreamable.FileWrapper(des);
            e.onNext(ipfs.addFile(Collections.singletonList(file)));
        }).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String hashCode) {
                        Log.e("Carefree", "uploadFileToIpfs onSuccess: " + hashCode);//QmV2V7MBRAxZub8931MB5LMRT6SwJQQfmqstmqz5dXLCbJ
                    }
                });

    }

    public static boolean isOverdue(long time) {
        return time*1000 < System.currentTimeMillis();
    }

    public static boolean isNotBegin(long time) {
        return time*1000 > System.currentTimeMillis();
    }

}
