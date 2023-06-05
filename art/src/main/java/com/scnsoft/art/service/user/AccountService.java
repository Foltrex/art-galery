package com.scnsoft.art.service.user;

import com.scnsoft.art.dto.AccountFilter;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.dto.UpdatePasswordRequest;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.repository.AccountRepository;
import com.scnsoft.art.repository.MetadataRepository;
import com.scnsoft.art.repository.specification.AccountSpecification;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.security.user.security.aop.AccountSecurityHandler;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.service.FileServiceImplFile;
import com.scnsoft.art.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final String OWNER_ORGANIZATION_ROLE = "CREATOR";
    @Value("${app.props.account.avatar_width}")
    private Integer avatarWidth;

    @Value("${app.props.account.avatar_height}")
    private Integer avatarHeight;

    private final List<String> administrativeOrganizationRoles = List.of(
            "CREATOR", "MODERATOR");

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MetadataRepository metadataRepository;
    private final AccountSecurityHandler accountSecurityHandler;
    private final FileServiceImplFile fileServiceImplFile;
    private final ArtService artService;

    public Page<Account> findAll(
            Pageable pageable,
            AccountFilter accountFilter
    ) {
        Specification<Account> generalSpecification = Specification.where(null);
        if (!(accountFilter.getName() == null || accountFilter.getName().isEmpty())) {
            generalSpecification = generalSpecification.and(AccountSpecification.firstnameOrLastnameStartWith(accountFilter.getName()));
        }
        if (accountFilter.getUsertype() != null) {
            generalSpecification = generalSpecification.and(AccountSpecification.usertypeEqual(accountFilter.getUsertype()));
        }
        if (accountFilter.getOrganizationId() != null) {
            generalSpecification = generalSpecification
                    .and(AccountSpecification.inMetadata(MetadataEnum.ORGANIZATION_ID.getValue(), accountFilter.getOrganizationId().toString()));
        }
        if (accountFilter.getCityId() != null) {
            generalSpecification = generalSpecification
                    .and(AccountSpecification.inMetadata(MetadataEnum.CITY_ID.getValue(), accountFilter.getCityId().toString()));
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
                            .and(AccountSpecification.inMetadata(MetadataEnum.ORGANIZATION_ID.getValue(), currentOrganizationIdString));
                    yield accountRepository.findAll(generalSpecification, pageable);
                } else {
                    yield new PageImpl<>(new ArrayList<>());
                }
            }
            case ARTIST -> new PageImpl<>(new ArrayList<>());
        };
    }

    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found by id: " + id));
    }

    public Account findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found by email: " + email));
    }

    public Account updateById(UUID id, Account account) {
        Account existedAccount = findById(id);
        var map = account.getMetadata().stream().collect(Collectors.toMap(v -> v.getMetadataId().getKey(), v -> v, (v1, v2) -> v1));

        var image = map.get(MetadataEnum.ACCOUNT_IMAGE.getValue());
        var existingImage = existedAccount.getMetadata().stream()
                .filter(m -> MetadataEnum.ACCOUNT_IMAGE.getValue().equals(m.getMetadataId().getKey()))
                .findAny()
                .orElse(null);

        var updateRequired = image != null && (existingImage == null || !existingImage.getValue().equals(image.getValue()));
        if (updateRequired) {
            var temp = fileServiceImplFile.findFileInfoById(UUID.fromString(image.getValue()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account image not found"));
            if (!FileServiceImplFile.temp.equals(temp.getDirectory())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update image, it is not a temporary file");
            }
            try (InputStream stream = fileServiceImplFile.getFileStream(temp.getId()).inputStream()) {

                byte[] imageData = ImageUtils.resizeImage(stream.readAllBytes(), temp.getMimeType(), avatarWidth, avatarHeight)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload image"));
                temp.setDirectory("/account/" + id + "/avatar/");
                temp.setCacheControl(-1);
                fileServiceImplFile.upstream(temp, imageData);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to persist avatar image");
            }
        }
        var deleteRequired = existingImage != null && (image == null || updateRequired);
        if (deleteRequired) {
            fileServiceImplFile.findFileInfoById(UUID.fromString(existingImage.getValue()))
                    .ifPresent(fileServiceImplFile::deleteFile);
        }

        metadataRepository.deleteAll(existedAccount.getMetadata()
                .stream()
                .filter(m -> !map.containsKey(m.getMetadataId().getKey())).collect(Collectors.toList()));


        existedAccount.setMetadata(account.getMetadata());
        existedAccount.setFirstName(account.getFirstName());
        existedAccount.setLastName(account.getLastName());
        var out = accountRepository.save(existedAccount);
        metadataRepository.saveAll(existedAccount.getMetadata());
        return out;
    }

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

    public void deleteById(UUID id) {
        Account account = this.findById(id);
        accountRepository.delete(account);
        artService.deleteByAccountId(id);
    }

    public boolean isSystemUser() {
        return findById(SecurityUtil.getCurrentAccountId()).getAccountType() == AccountType.SYSTEM;
    }
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
