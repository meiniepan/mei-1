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
    private String name;
    @Property(nameInDb = "PHONE")
    private String mobile;
    @Property(nameInDb = "UID")
    private String uid;
    @Property(nameInDb = "HEAD")
    private String head_image;
    @Property(nameInDb = "TOKEN")
    private String token;
    @Property(nameInDb = "PWD")
    private String password;
    @Property(nameInDb = "GENDER")
    private String gender;

    @Generated(hash = 874816226)
    public UserInfo(long mid, String name, String mobile, String uid,
            String head_image, String token, String password, String gender) {
        this.mid = mid;
        this.name = name;
        this.mobile = mobile;
        this.uid = uid;
        this.head_image = head_image;
        this.token = token;
        this.password = password;
        this.gender = gender;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public long getMid() {
        return this.mid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMid(long mid) {

        this.mid = mid;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
