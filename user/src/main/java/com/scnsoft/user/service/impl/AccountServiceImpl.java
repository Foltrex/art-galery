package com.scnsoft.user.service.impl;

import static com.scnsoft.user.repository.specification.AccountSpecification.idInList;
import static com.scnsoft.user.repository.specification.AccountSpecification.firstnameOrLastnameStartWith;
import static com.scnsoft.user.repository.specification.AccountSpecification.usertypeEqual;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.netflix.servo.util.Strings;
import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.OrganizationDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import com.scnsoft.user.entity.Account.AccountType;
import com.scnsoft.user.entity.constant.MetadataEnum;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.FileFeignClient;
import com.scnsoft.user.feignclient.OrganizationFeignClient;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.MetadataRepository;
import com.scnsoft.user.security.aop.AccountSecurityHandler;
import com.scnsoft.user.service.AccountService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final String ORGANIZATION_ROLE_KEY = "organization_role";
    private static final String ORGANIZATION_ID_KEY = "organization_id";
    private static final String OWNER_ORGANIZATION_ROLE = "CREATOR";

    private final List<String> administrativeOrganizationRoles = List.of(
            "CREATOR", "MODERATOR");

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileFeignClient fileFeignClient;
    private final MetadataRepository metadataRepository;
    private final AccountSecurityHandler accountSecurityHandler;
    private final OrganizationFeignClient organizationFeignClient;

    @Override
    public Page<Account> findAll(Pageable pageable) {
        Account loggedUser = accountSecurityHandler.getCurrentAccount();
        return switch (loggedUser.getAccountType()) {
            case SYSTEM -> accountRepository.findAll(pageable);
            case REPRESENTATIVE -> {
                String organizationRole = metadataRepository
                        .findByMetadataIdAccountIdAndMetadataIdKey(
                                loggedUser.getId(), ORGANIZATION_ROLE_KEY)
                        .map(Metadata::getValue)
                        .orElseThrow();

                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    String currentOrganizationIdString = loggedUser.getMetadata()
                            .stream()
                            .filter(m -> m.getMetadataId().getKey().equals(ORGANIZATION_ID_KEY))
                            .findFirst()
                            .map(Metadata::getValue)
                            .orElseThrow();

                    List<UUID> accountIds = metadataRepository
                            .findByMetadataIdKeyAndValue(ORGANIZATION_ID_KEY, currentOrganizationIdString)
                            .stream()
                            .map(m -> m.getMetadataId().getAccountId())
                            .toList();
                    yield accountRepository.findAll(idInList(accountIds), pageable);
                } else {
                    throw new IllegalArgumentException(
                            "Accountss's not allowed for " + organizationRole + " representative role");
                }
            }
            case ARTIST -> throw new IllegalArgumentException("Accountss's not allowed for artist");
        };
    }

    @Override
    public Page<Account> findAllByOrganizationId(UUID organizationId, Pageable pageable) {
        return accountRepository.findAllByOrganizationId(String.valueOf(organizationId), pageable);
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
    @Transactional
    public void deleteById(UUID id) {
        Account account = findById(id);
        accountRepository.delete(account);
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

    @Override
    public Page<Account> findAll(
            Pageable pageable,
            String username,
            String usertype,
            String organiationName,
            UUID cityId) {
        Specification<Account> generalSpecification = Specification.where(null);
        if (!Strings.isNullOrEmpty(username)) {
            generalSpecification = generalSpecification.and(firstnameOrLastnameStartWith(username));
        }
        if (!Strings.isNullOrEmpty(usertype)) {
            AccountType accountType = AccountType.valueOf(usertype);
            generalSpecification = generalSpecification.and(usertypeEqual(accountType));
        }
        if (!Strings.isNullOrEmpty(organiationName)) {
            OrganizationDto organizationDto = organizationFeignClient.findByName(organiationName);
            if (organizationDto != null) {
                List<UUID> accountIds = metadataRepository
                        .findByMetadataIdKeyAndValue(ORGANIZATION_ID_KEY, String.valueOf(organizationDto.getId()))
                        .stream()
                        .map(m -> {
                            var metadataId = m.getMetadataId();
                            return metadataId.getAccountId();
                        })
                        .toList();
                generalSpecification = generalSpecification.and(idInList(accountIds));
            } else {
                return Page.empty(pageable);
            }
        }
        if (cityId != null) {
            List<UUID> accountIds = metadataRepository
                .findByMetadataIdKeyAndValue(
                    "city_id", 
                    String.valueOf(cityId)
                )
                .stream()
                .map(m -> {
                    var metadataId = m.getMetadataId();
                    return metadataId.getAccountId();
                })
                .toList();

                generalSpecification = generalSpecification.and(idInList(accountIds));
        }

        Account loggedUser = accountSecurityHandler.getCurrentAccount();
        return switch (loggedUser.getAccountType()) {
            case SYSTEM -> accountRepository.findAll(generalSpecification, pageable);
            case REPRESENTATIVE -> {
                String organizationRole = metadataRepository
                        .findByMetadataIdAccountIdAndMetadataIdKey(
                                loggedUser.getId(), ORGANIZATION_ROLE_KEY)
                        .map(Metadata::getValue)
                        .orElseThrow();

                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    String currentOrganizationIdString = loggedUser.getMetadata()
                            .stream()
                            .filter(m -> m.getMetadataId().getKey().equals(ORGANIZATION_ID_KEY))
                            .findFirst()
                            .map(Metadata::getValue)
                            .orElseThrow();

                    List<UUID> accountIds = metadataRepository
                            .findByMetadataIdKeyAndValue(ORGANIZATION_ID_KEY, currentOrganizationIdString)
                            .stream()
                            .map(m -> m.getMetadataId().getAccountId())
                            .toList();

                    generalSpecification = generalSpecification.and(idInList(accountIds));
                    yield accountRepository.findAll(generalSpecification, pageable);
                } else {
                    throw new IllegalArgumentException(
                            "Accountss's not allowed for " + organizationRole + " representative role");
                }
            }
            case ARTIST -> throw new IllegalArgumentException("Accountss's not allowed for artist");
        };
    }

}
