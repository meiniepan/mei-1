package com.wuyou.user.data.local.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by DELL on 2018/5/9.
 */
@Entity
public class SearchHistoryBean {
    @Id
    private Long mid;
    @Property(nameInDb = "TITLE")
    private String title;

    public SearchHistoryBean(String searchText) {
        title = searchText;
    }

    @Generated(hash = 1307944646)
    public SearchHistoryBean(Long mid, String title) {
        this.mid = mid;
        this.title = title;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }

    public Long getMid() {
        return this.mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
