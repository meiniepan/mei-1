package com.wuyou.user.data.local.db;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by swapnibble on 2017-12-08.
 */
@Entity
public class EosAccount {
    public static final int TYPE_ACCOUNT_ALL = 0;
    public static final int TYPE_ACCOUNT_USER = 1;
    public static final int TYPE_ACCOUNT_CONTRACT = 2;

    @NonNull
    @Id
    @Property(nameInDb = "name")
    public String   name;

    @Property(nameInDb = "type")
    public Integer  type;

    public static EosAccount from( String name){
        return new EosAccount(name, TYPE_ACCOUNT_ALL );
    }

    @Keep
    public EosAccount( String name, Integer type){
        this.name = name;
        this.type = type;
    }

    @Generated(hash = 1777469907)
    public EosAccount() {
    }

    @Override
    public int hashCode(){
        int result = 0;

        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type : 0);

        return result;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
