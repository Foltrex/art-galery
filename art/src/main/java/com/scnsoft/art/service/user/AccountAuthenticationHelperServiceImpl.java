package com.scnsoft.art.service.user;

import com.scnsoft.art.dto.AuthToken;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Role;
import com.scnsoft.art.repository.RoleRepository;
import com.scnsoft.art.security.JwtUtils;
import com.scnsoft.art.security.user.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AccountAuthenticationHelperServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public void setAccountToAuthentication(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public AuthToken createAuthTokenResponse(Account account) {
        return AuthToken.builder()
                .token(createToken(account))
                .type("Bearer")
                .build();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Set<Role> getUserRoles() {
        return Set.of(roleRepository
                .findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")));
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
