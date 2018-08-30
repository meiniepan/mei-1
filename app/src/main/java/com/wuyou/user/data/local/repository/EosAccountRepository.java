package com.wuyou.user.data.local.repository;

import com.wuyou.user.data.local.db.EosAccount;

import java.util.List;

/**
 * Created by swapnibble on 2017-12-14.
 */

public interface EosAccountRepository {
    void addAll(String... accountNames);
    void addAll(List<String> accountNames);
    void addAccount(String accountName);
    void deleteAll();
    void delete(String accountName);

    /**
     * get account list
     * @return
     */
    List<EosAccount> getAllEosAccount();

    EosAccount searchName(String nameStarts);
}
