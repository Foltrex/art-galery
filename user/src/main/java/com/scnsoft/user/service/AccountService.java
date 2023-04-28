package com.scnsoft.user.service;

import com.scnsoft.user.dto.AccountFilter;
import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface AccountService {

    Page<Account> findAll(Pageable pageable, AccountFilter accountFilter);

    Account findById(UUID id);

    Account findByEmail(String email);

    Account updateById(UUID id, Account account);

    void updatePasswordById(UUID id, UpdatePasswordRequest updatePasswordRequest);

    Metadata updateImageById(UUID id, UploadFileDto uploadFileDto);

    void deleteById(UUID id);


    boolean isEditingUser(UUID activeUserId, UUID targetUserId);
}
