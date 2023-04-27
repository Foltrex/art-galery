package com.scnsoft.user.app;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("/application.yml")
@Slf4j
public class AppInitialization {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.props.admin.email}")
    private String adminEmail;

    @Value("${app.props.admin.password}")
    private String adminPassword;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        log.info("Initialization method has started");
        initRoles();
        initAdminAccount();
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
                    .accountType(Account.AccountType.SYSTEM)
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
                        .orElseThrow(() -> new ResourseNotFoundException("Role not found!")),
                roleRepository
                        .findByName(Role.RoleType.ROLE_USER)
                        .orElseThrow(() -> new ResourseNotFoundException("Role not found!"))
        );
    }


}
