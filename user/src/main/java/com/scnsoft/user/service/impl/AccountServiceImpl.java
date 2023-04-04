package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import com.scnsoft.user.entity.constant.MetadataEnum;
import com.scnsoft.user.exception.FeignResponseException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.FileFeignClient;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.MetadataRepository;
import com.scnsoft.user.service.AccountService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final String ORGANIZATION_ROLE_KEY = "organization_role";
    private static final String OWNER_ORGANIZATION_ROLE = "CREATOR";

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileFeignClient fileFeignClient;
    private final MetadataRepository metadataRepository;

    @Override
    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by id: " + id));
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by email: " + email));
    }

    @Override
    public Account updateById(UUID id, Account account) {
        Account existedAccount = findById(id);
        existedAccount.setMetadata(account.getMetadata());
        existedAccount.setFirstName(account.getFirstName());
        existedAccount.setLastName(account.getLastName());

        return accountRepository.save(existedAccount);
    }

    @Override
    public void updatePasswordById(UUID id, UpdatePasswordRequest updatePasswordRequest) {
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();
        Account account = findById(id);

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is not correct!");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void updateImageById(UUID id, UploadFileDto uploadFileDto) {
        Account account = findById(id);
        try {
            FileInfoDto fileInfoDto = fileFeignClient.uploadFile(uploadFileDto);
            List<Metadata> metadataList = account.getMetadata();
            Optional<Metadata> metadataAccountOldImageOptional = metadataList
                    .stream()
                    .filter(metadata -> metadata.getMetadataId()
                            .getKey().equals(MetadataEnum.ACCOUNT_IMAGE.getValue()))
                    .findFirst();

            if (metadataAccountOldImageOptional.isPresent()) {
                Metadata metadataAccountOldImage = metadataAccountOldImageOptional.get();
                metadataRepository.updateValueById
                        (metadataAccountOldImage.getMetadataId(), fileInfoDto.getId().toString());
                fileFeignClient.removeFile(metadataAccountOldImage.getValue());
            } else {
                Metadata metadata = Metadata.builder()
                        .metadataId(MetadataId.builder()
                                .accountId(id)
                                .key(MetadataEnum.ACCOUNT_IMAGE.getValue())
                                .build())
                        .value(fileInfoDto.getId().toString())
                        .build();

                metadataRepository.save(metadata);
            }

        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.status()), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Account account = findById(id);
        accountRepository.delete(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public boolean isEditingUser(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow();

        return switch (account.getAccountType()) {
            case REPRESENTATIVE -> {
                Metadata metadata = metadataRepository
                        .findByMetadataIdAccountIdAndMetadataIdKey(id, ORGANIZATION_ROLE_KEY)
                        .orElseThrow();
                yield OWNER_ORGANIZATION_ROLE.equals(metadata.getValue());
            }
            case SYSTEM -> true;
            case ARTIST -> false;
            default -> false;
        };
    }

}
