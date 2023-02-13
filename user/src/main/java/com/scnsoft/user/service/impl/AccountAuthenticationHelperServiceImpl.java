package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.repository.RoleRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.security.UserDetailsServiceImpl;
import com.scnsoft.user.service.AccountAuthenticationHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AccountAuthenticationHelperServiceImpl implements AccountAuthenticationHelperService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void setAccountToAuthentication(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Account createAccount(String email, String password, Account.AccountType accountType) {
        return Account.builder()
                .email(email)
                .password(encodePassword(password))
                .accountType(accountType)
                .roles(getUserRoles())
                .build();
    }

    @Override
    public AuthToken createAuthTokenResponse(Account account) {
        return AuthToken.builder()
                .token(createToken(account))
                .type("Bearer")
                .build();
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Set<Role> getUserRoles() {
        return Set.of(roleRepository
                .findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new ResourseNotFoundException("Role not found!")));
    }

    private String createToken(Account account) {
        return jwtUtils.createToken(
                account.getEmail(),
                account.getId(),
                account.getAccountType(),
                account.getRoles()
        );
    }

}
