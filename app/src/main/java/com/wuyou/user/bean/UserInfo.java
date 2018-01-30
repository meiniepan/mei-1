package com.wuyou.user.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018\1\25 0025.
 */
@Entity
public class UserInfo {
    @Id
    private long mid;
    @Property(nameInDb = "USERNAME")
    private String userName;
    @Property(nameInDb = "PHONE")
    private String phone;

    private String token;

    public void setId(long id) {
        this.mid = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    @Generated(hash = 305108212)
    public UserInfo(long mid, String userName, String phone, String token) {
        this.mid = mid;
        this.userName = userName;
        this.phone = phone;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return this.mid;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getMid() {
        return this.mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }
}
