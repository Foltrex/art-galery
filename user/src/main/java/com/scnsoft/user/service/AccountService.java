package com.scnsoft.user.service;

import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.payload.DeleteAccountRequest;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;

import java.util.UUID;

public interface AccountService {

    Account findById(UUID id);

    Account findByEmail(String email);

    void deleteAccountById(DeleteAccountRequest deleteAccountRequest);

}
