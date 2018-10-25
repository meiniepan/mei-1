package com.wuyou.user.data.local.db;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DELL on 2018/10/23.
 */
@Entity
public class TraceIPFSBean {
    @Id
    public Long id;
    /**
     * timestamp: 2018-10-22T10:00:00
     * account_name: samsamsamsam
     * phone: 18700000000
     * node_name: wuyou
     * content: "Hello"
     * picture: QmefdCKsLhfdSLxEH9k8ediJ3pRtBsq788rhAuh63UX9xr QmefdCKsLhfdSLxEH9k8ediJ3pRtBsq788rhAuh63UX9xr
     */
    @Property(nameInDb = "TIMESTAMP")
    public String timestamp;
    @Property(nameInDb = "ACCOUNT_NAME")
    public String account_name;
    @Property(nameInDb = "PHONE")
    public String phone;
    @Property(nameInDb = "NODE_NAME")
    public String node_name;
    @Property(nameInDb = "CONTENT")
    public String content;
    @Property(nameInDb = "PICTURE")
    @Convert(converter = ListConverter.class, columnType = String.class)
    public List<String> picture;
    @Property(nameInDb = "STATUS")
    public int status = -1;  //-1 待审核  0 待确认  1 已完成
    @Property(nameInDb = "KEYWORD")
    public String keyword;


    @Generated(hash = 2030500379)
    public TraceIPFSBean(Long id, String timestamp, String account_name, String phone, String node_name,
            String content, List<String> picture, int status, String keyword) {
        this.id = id;
        this.timestamp = timestamp;
        this.account_name = account_name;
        this.phone = phone;
        this.node_name = node_name;
        this.content = content;
        this.picture = picture;
        this.status = status;
        this.keyword = keyword;
    }


    @Generated(hash = 1362390416)
    public TraceIPFSBean() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTimestamp() {
        return this.timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getAccount_name() {
        return this.account_name;
    }


    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }


    public String getPhone() {
        return this.phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getNode_name() {
        return this.node_name;
    }


    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public List<String> getPicture() {
        return this.picture;
    }


    public void setPicture(List<String> picture) {
        this.picture = picture;
    }


    public int getStatus() {
        return this.status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public String getKeyword() {
        return this.keyword;
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    static class ListConverter implements PropertyConverter<List<String>, String> {
        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            } else {
                return Arrays.asList(databaseValue.split(","));
            }
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            if (entityProperty == null) {
                return null;
            } else {
                StringBuilder sb = new StringBuilder();
                for (String link : entityProperty) {
                    sb.append(link);
                    sb.append(",");
                }
                return sb.toString();
            }
        }
    }
}
