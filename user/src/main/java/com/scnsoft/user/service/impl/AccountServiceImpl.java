package com.scnsoft.user.service.impl;

import com.netflix.servo.util.Strings;
import com.scnsoft.user.dto.AccountFilter;
import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import com.scnsoft.user.entity.Account.AccountType;
import com.scnsoft.user.entity.constant.MetadataEnum;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.ArtFeignClient;
import com.scnsoft.user.feignclient.FileFeignClient;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.MetadataRepository;
import com.scnsoft.user.security.aop.AccountSecurityHandler;
import com.scnsoft.user.service.AccountService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.scnsoft.user.repository.specification.AccountSpecification.firstnameOrLastnameStartWith;
import static com.scnsoft.user.repository.specification.AccountSpecification.inMetadata;
import static com.scnsoft.user.repository.specification.AccountSpecification.usertypeEqual;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String OWNER_ORGANIZATION_ROLE = "CREATOR";

    private final List<String> administrativeOrganizationRoles = List.of(
            "CREATOR", "MODERATOR");

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileFeignClient fileFeignClient;
    private final ArtFeignClient artFeignClient;
    private final MetadataRepository metadataRepository;
    private final AccountSecurityHandler accountSecurityHandler;


    @Override
    public Page<Account> findAll(
            Pageable pageable,
            AccountFilter accountFilter
    ) {
        Specification<Account> generalSpecification = Specification.where(null);
        if (!Strings.isNullOrEmpty(accountFilter.getUsername())) {
            generalSpecification = generalSpecification.and(firstnameOrLastnameStartWith(accountFilter.getUsername()));
        }
        if (accountFilter.getUsertype() != null) {
            generalSpecification = generalSpecification.and(usertypeEqual(accountFilter.getUsertype()));
        }
        if (accountFilter.getOrganizationId() != null) {
            generalSpecification = generalSpecification
                    .and(inMetadata(MetadataEnum.ORGANIZATION_ID.getValue(), accountFilter.getOrganizationId().toString()));
        }
        if (accountFilter.getCityId() != null) {
            generalSpecification = generalSpecification
                    .and(inMetadata(MetadataEnum.CITY_ID.getValue(), accountFilter.getCityId().toString()));
        }

        Account loggedUser = accountSecurityHandler.getCurrentAccount();
        return switch (loggedUser.getAccountType()) {
            case SYSTEM -> accountRepository.findAll(generalSpecification, pageable);
            case REPRESENTATIVE -> {
                String organizationRole = metadataRepository
                        .findByMetadataIdAccountIdAndMetadataIdKey(
                                loggedUser.getId(), MetadataEnum.ORGANIZATION_ROLE.getValue())
                        .map(Metadata::getValue)
                        .orElseThrow();

                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    String currentOrganizationIdString = loggedUser.getMetadata()
                            .stream()
                            .filter(m -> m.getMetadataId().getKey().equals(MetadataEnum.ORGANIZATION_ID.getValue()))
                            .findFirst()
                            .map(Metadata::getValue)
                            .orElseThrow();

                    generalSpecification = generalSpecification
                            .and(inMetadata(MetadataEnum.ORGANIZATION_ID.getValue(), currentOrganizationIdString));
                    yield accountRepository.findAll(generalSpecification, pageable);
                } else {
                    throw new IllegalArgumentException(
                            "Accountss's not allowed for " + organizationRole + " representative role");
                }
            }
            case ARTIST -> throw new IllegalArgumentException("Accountss's not allowed for artist");
        };
    }

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
                metadataRepository.updateValueById(metadataAccountOldImage.getMetadataId(),
                        fileInfoDto.getId().toString());
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
    public void deleteById(UUID id) {
        Account account = this.findById(id);
        if (account.getAccountType() == AccountType.ARTIST) {
            artFeignClient.deleteByAccountId(id);
        }

        accountRepository.delete(account);
    }

    @Override
    public boolean isEditingUser(UUID activeUserId, UUID targetUserId) {
        Account account = accountRepository.findById(activeUserId)
                .orElseThrow();
        if (activeUserId.equals(targetUserId)) {
            return true;
        }

        return switch (account.getAccountType()) {
            case REPRESENTATIVE -> {
                Metadata metadata = metadataRepository
                        .findByMetadataIdAccountIdAndMetadataIdKey(activeUserId, MetadataEnum.ORGANIZATION_ROLE.getValue())
                        .orElseThrow();
                yield OWNER_ORGANIZATION_ROLE.equals(metadata.getValue());
            }
            case SYSTEM -> true;
            case ARTIST -> false;
            default -> false;
        };
    }


}
