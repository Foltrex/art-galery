package com.scnsoft.user.service;

import com.scnsoft.user.entity.Account;

import java.util.UUID;

public interface AccountService {

    Account findById(UUID id);

    Account findByEmail(String email);

    void deleteById(UUID id);

}
