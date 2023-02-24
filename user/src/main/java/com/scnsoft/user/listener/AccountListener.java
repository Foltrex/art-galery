package com.scnsoft.user.listener;

import com.scnsoft.user.entity.Account;

import javax.persistence.PrePersist;


public class AccountListener {

    @PrePersist
    private void beforePersist(Account account) {
        account.setFailCount(0);
    }

}
