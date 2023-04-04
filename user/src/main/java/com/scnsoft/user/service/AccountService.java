package com.scnsoft.user.service;

import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface AccountService {

    Account findById(UUID id);

    Account findByEmail(String email);

    Account updateById(UUID id, Account account);

    void updatePasswordById(UUID id, UpdatePasswordRequest updatePasswordRequest);

    void updateImageById(UUID id, UploadFileDto uploadFileDto);

    void deleteById(UUID id);

    Page<Account> findAll(Pageable pageable);

    boolean isEditingUser(UUID id);
}
