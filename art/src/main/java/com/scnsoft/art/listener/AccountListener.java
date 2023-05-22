package com.scnsoft.art.listener;


import com.scnsoft.art.entity.Account;

import javax.persistence.PrePersist;


public class AccountListener {

    @PrePersist
    private void beforePersist(Account account) {
        account.setFailCount(0);
    }

}
