package com.scnsoft.art.app;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.entity.Role;
import com.scnsoft.art.repository.AccountRepository;
import com.scnsoft.art.repository.CurrencyRepository;
import com.scnsoft.art.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppInitialization {
    private static final String DOLLAR_CURRENCY_VALUE = "USD";
    private static final String DOLLAR_CURRENCY_LABEL = "$";

    public static final String GEORGIAN_CURRENCY_VALUE = "GEL";
    private static final String GEORGIAN_CURRENCY_LABEL = "ლ";

    private final CurrencyRepository currencyRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.props.admin.email}")
    private String adminEmail;

    @Value("${app.props.admin.password}")
    private String adminPassword;

    @Value("${app.props.admin.do-init}")
    private Boolean initAdmin;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        initCurrencies();
        initRoles();
        if(Boolean.TRUE.equals(initAdmin)) {
            initAdminAccount();
        }
    }

    @Transactional
    public void initCurrencies() {
        if (currencyRepository.findByValue(DOLLAR_CURRENCY_VALUE).isEmpty()) {
            Currency dollarCurrency = Currency.builder()
                    .value(DOLLAR_CURRENCY_VALUE)
                    .label(DOLLAR_CURRENCY_LABEL)
                    .build();

            currencyRepository.save(dollarCurrency);
        }

        if (currencyRepository.findByValue(GEORGIAN_CURRENCY_VALUE).isEmpty()) {
            Currency georgianCurrency = Currency.builder()
                    .value(GEORGIAN_CURRENCY_VALUE)
                    .label(GEORGIAN_CURRENCY_LABEL)
                    .build();

            currencyRepository.save(georgianCurrency);
        }
    }


    @Transactional
    public void initRoles() {
        log.info("Initialization roles");
        if (roleRepository.findByName(Role.RoleType.ROLE_ADMIN).isEmpty()) {
            Role roleAdmin = Role.builder().name(Role.RoleType.ROLE_ADMIN).build();
            roleRepository.save(roleAdmin);
        }
        if (roleRepository.findByName(Role.RoleType.ROLE_MODERATOR).isEmpty()) {
            Role roleModerator = Role.builder().name(Role.RoleType.ROLE_MODERATOR).build();
            roleRepository.save(roleModerator);
        }
        if (roleRepository.findByName(Role.RoleType.ROLE_USER).isEmpty()) {
            Role roleUser = Role.builder().name(Role.RoleType.ROLE_USER).build();
            roleRepository.save(roleUser);
        }
    }

    private void initAdminAccount() {
        log.info("Admin account initialization");
        accountRepository.findByEmail(adminEmail).orElseGet(() -> {
            Account account = Account.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .failCount(0)
                    .roles(getAdminRoles())
                    .accountType(AccountType.SYSTEM)
                    .isOneTimePassword(false)
                    .build();

            account = accountRepository.save(account);
            log.error("New admin account created, please login and change password");
            return account;
        });
    }

    private Set<Role> getAdminRoles() {
        return Set.of(
                roleRepository
                        .findByName(Role.RoleType.ROLE_ADMIN)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")),
                roleRepository
                        .findByName(Role.RoleType.ROLE_USER)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"))
        );
    }


}
