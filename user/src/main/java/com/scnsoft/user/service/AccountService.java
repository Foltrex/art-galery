package com.scnsoft.user.service;

import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface AccountService {

    Page<Account> findAll(Pageable pageable);

    Page<Account> findAll(
        Pageable pageable, 
        String username, 
        String usertype, 
        String organiationName, 
        UUID cityId
    );

    Page<Account> findAllByOrganizationId(UUID organizationId, Pageable pageable);

    Account findById(UUID id);

    Account findByEmail(String email);

    Account updateById(UUID id, Account account);

    void updatePasswordById(UUID id, UpdatePasswordRequest updatePasswordRequest);

    void updateImageById(UUID id, UploadFileDto uploadFileDto);

    void deleteById(UUID id);


    boolean isEditingUser(UUID id);
}
