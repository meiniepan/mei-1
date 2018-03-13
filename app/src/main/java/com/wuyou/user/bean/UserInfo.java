package com.wuyou.user.bean;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Administrator on 2018\1\25 0025.
 */
@Entity
public class UserInfo {
    @Id
    private Long mid;
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
    @Property(nameInDb = "ADDRESS")
    @Convert(converter = AddressConverter.class, columnType = String.class)
    private AddressBean address;

    @Generated(hash = 360151486)
    public UserInfo(Long mid, String name, String mobile, String uid, String head_image,
            String token, String password, String gender, AddressBean address) {
        this.mid = mid;
        this.name = name;
        this.mobile = mobile;
        this.uid = uid;
        this.head_image = head_image;
        this.token = token;
        this.password = password;
        this.gender = gender;
        this.address = address;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public Long getMid() {
        return this.mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHead_image() {
        return this.head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public AddressBean getAddress() {
        return this.address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public static class AddressConverter implements PropertyConverter<AddressBean, String> {

        @Override
        public AddressBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) return null;
            return new Gson().fromJson(databaseValue, AddressBean.class);
        }

        @Override
        public String convertToDatabaseValue(AddressBean entityProperty) {
            if (entityProperty == null) return null;
            return new Gson().toJson(entityProperty);
        }
    }
}
