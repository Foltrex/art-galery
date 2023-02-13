package com.scnsoft.user.listener;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.EmailMessageCode;

import javax.persistence.PrePersist;
import java.util.Date;

public class EmailMessageCodeListener {

    @PrePersist
    private void beforePersist(EmailMessageCode emailMessageCode) {
        emailMessageCode.setCountAttempts(0);
        emailMessageCode.setIsValid(true);
        emailMessageCode.setCreatedAt(new Date());
    }

}
