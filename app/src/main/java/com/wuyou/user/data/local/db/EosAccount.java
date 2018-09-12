package com.wuyou.user.data.local.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by DELL on 2017-12-08.
 */
@Entity
public class EosAccount {
    public static final int TYPE_ACCOUNT_ALL = 0;
    public static final int TYPE_ACCOUNT_USER = 1;
    public static final int TYPE_ACCOUNT_CONTRACT = 2;

    @Id
    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "type")
    private Integer type;

    @Property(nameInDb = "private")
    private String privateKey;

    @Property(nameInDb = "public")
    private String publicKey;

    @Property(nameInDb = "main")
    private Boolean main;

    public static EosAccount from(String name) {
        return new EosAccount(name, TYPE_ACCOUNT_ALL);
    }

    @Keep
    public EosAccount(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    @Generated(hash = 1777469907)
    public EosAccount() {
    }

    @Generated(hash = 1244585924)
    public EosAccount(String name, Integer type, String privateKey,
                      String publicKey, Boolean main) {
        this.name = name;
        this.type = type;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.main = main;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type : 0);

        return result;
    }

    @Override
    public String toString() {
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

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Boolean getMain() {
        return this.main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
}
