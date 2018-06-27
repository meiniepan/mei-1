package com.wuyou.user;

import android.text.TextUtils;

import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.DaoMaster;
import com.wuyou.user.bean.DaoSession;
import com.wuyou.user.bean.SearchHistoryBean;
import com.wuyou.user.bean.SearchHistoryBeanDao;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.bean.UserInfoDao;

import java.util.List;

/**
 * Created by hjn on 2018/3/8.
 */

public class CarefreeDaoSession {
    private static DaoSession daoSession;
    private static CarefreeDaoSession instance;
    public static String tempAvatar;

    private CarefreeDaoSession() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(CarefreeApplication.getInstance().getApplicationContext(), "carefree.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

    public static synchronized CarefreeDaoSession getInstance() {
        if (null == instance) {
            instance = new CarefreeDaoSession();
        }
        return instance;
    }

    public static String getAvatar(UserInfo info) {
        return tempAvatar == null ? info.getAvatar() : tempAvatar;
    }

    private UserInfoDao getUserInfoDao() {
        return daoSession.getUserInfoDao();
    }

    public void setUserInfo(UserInfo userInfo) {
        getUserInfoDao().insert(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        getUserInfoDao().update(userInfo);
    }

    public void clearUserInfo() {
        getUserInfoDao().deleteAll();
        uid = null;
    }

    public UserInfo getUserInfo() {
        List<UserInfo> userInfos = getUserInfoDao().loadAll();
        if (userInfos == null || userInfos.size() == 0) return null;
        return userInfos.get(0);
    }

    private String uid;

    public String getUserId() {
        if (TextUtils.isEmpty(uid)) {
            List<UserInfo> userInfos = getUserInfoDao().loadAll();
            if (userInfos == null || userInfos.size() == 0) return uid;
            uid = userInfos.get(0).getUid();
            return uid;
        } else {
            return uid;
        }
    }

    public void saveDefaultAddress(AddressBean addressBean) {
        UserInfo userInfo = getUserInfo();
        userInfo.setAddress(addressBean);
        getUserInfoDao().update(userInfo);
    }

    public AddressBean getDefaultAddress() {
        return getUserInfo().getAddress();
    }


    public void addHistoryRecord(SearchHistoryBean bean) {
        SearchHistoryBeanDao searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();
        if (searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Title.eq(bean.getTitle())).list().size() == 0)
            searchHistoryBeanDao.save(bean);
    }

    public List<SearchHistoryBean> getHistoryRecords() {
        return daoSession.getSearchHistoryBeanDao().queryBuilder().orderDesc(SearchHistoryBeanDao.Properties.Mid).build().list();
    }

    public void clearSearchHistory() {
        daoSession.getSearchHistoryBeanDao().deleteAll();
    }

    public void deleteHistory(SearchHistoryBean item) {
        daoSession.getSearchHistoryBeanDao().delete(item);
    }
}
