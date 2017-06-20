package com.haofeng.apps.dst;

import com.haofeng.apps.dst.bean.Account;

/**
 *
 * Created by WIN10 on 2017/6/13.
 */

public class UserDataManager {
    public static UserDataManager manager = new UserDataManager();

    private UserDataManager() {}

    public static UserDataManager getInstance() {
        if(manager == null) {
            manager = new UserDataManager();
        }
        return manager;
    }

    private Account mAccount = new Account();

    public void setAccount(Account account) {
        mAccount = account;
    }

    public Account getAccount() {
        return mAccount;
    }





}
