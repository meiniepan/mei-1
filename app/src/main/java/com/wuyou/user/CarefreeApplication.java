package com.wuyou.user;

import android.os.Environment;
import android.text.TextUtils;

import com.gs.buluo.common.BaseApplication;
import com.wuyou.user.bean.DaoMaster;
import com.wuyou.user.bean.DaoSession;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.bean.UserInfoDao;

import java.util.List;

/**
 * Created by hjn on 2016/11/1.
 */
public class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;
    private UserInfoDao userInfoDao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDB();
    }

    private void initDB() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "carefree.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userInfoDao = daoSession.getUserInfoDao();
    }

    public static synchronized CarefreeApplication getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {

    }


    @Override
    public String getFilePath() {
        return Environment.getExternalStorageDirectory().toString() + "/carefree/";
    }

    public UserInfo getUserInfo() {
        List<UserInfo> userInfos = userInfoDao.loadAll();
        if (userInfos == null || userInfos.size() == 0) return null;
        return userInfos.get(0);
    }

    private String uid;

    public String getUserId() {
        if (TextUtils.isEmpty(uid)) {
            List<UserInfo> userInfos = userInfoDao.loadAll();
            if (userInfos == null || userInfos.size() == 0) return null;
            uid = userInfos.get(0).getUid();
            return uid;
        } else {
            return uid;
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        CarefreeApplication.getInstance().getUserInfoDao().insert(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        CarefreeApplication.getInstance().getUserInfoDao().update(userInfo);
    }

    public void clearUserInfo() {
        uid = null;
        userInfoDao.deleteAll();
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }
}
