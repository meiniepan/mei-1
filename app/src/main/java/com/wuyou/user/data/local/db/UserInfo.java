package com.wuyou.user.data.local.db;

import com.google.gson.Gson;
import com.wuyou.user.data.remote.AddressBean;

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
    private String nickname;
    @Property(nameInDb = "PHONE")
    private String mobile;
    @Property(nameInDb = "UID")
    private String uid;
    @Property(nameInDb = "HEAD")
    private String avatar;
    @Property(nameInDb = "TOKEN")
    private String token;
    @Property(nameInDb = "PWD")
    private String password;
    @Property(nameInDb = "GENDER")
    private String gender;
    @Property(nameInDb = "ADDRESS")
    @Convert(converter = AddressConverter.class, columnType = String.class)
    private AddressBean address;

    private String email;
    private String birthday;

    private long received_points;

    private long out_points;

    @Generated(hash = 29014789)
    public UserInfo(Long mid, String nickname, String mobile, String uid, String avatar,
            String token, String password, String gender, AddressBean address, String email,
            String birthday, long received_points, long out_points) {
        this.mid = mid;
        this.nickname = nickname;
        this.mobile = mobile;
        this.uid = uid;
        this.avatar = avatar;
        this.token = token;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.received_points = received_points;
        this.out_points = out_points;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public Long getMid() {
        return this.mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getName() {
        return this.nickname;
    }

    public void setName(String name) {
        this.nickname = name;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getReceived_points() {
        return received_points;
    }

    public void setReceived_points(long received_points) {
        this.received_points = received_points;
    }

    public long getOut_points() {
        return out_points;
    }

    public void setOut_points(long out_points) {
        this.out_points = out_points;
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
