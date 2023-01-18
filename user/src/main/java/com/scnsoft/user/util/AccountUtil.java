package com.scnsoft.user.util;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AccountUtil {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Account createAccount(String email, String password, Account.AccountType accountType) {
        return Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .accountType(accountType)
                .roles(getUserRoles())
                .build();
    }

    private Set<Role> getUserRoles() {
        return Set.of(roleRepository
                .findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new ResourseNotFoundException("Role not found!")));
    }

}
