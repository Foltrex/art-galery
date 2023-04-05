package com.scnsoft.art.security;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.feignclient.AccountFeignClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtUtils.parseJwtToken(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                UUID id = jwtUtils.getIdFromJwtToken(token);
                String email = jwtUtils.getEmailFromJwtToken(token);
                List<String> roles = jwtUtils.getRolesFromJwtToken(token);

                // try {
                //     Page<AccountDto> accountDtoResponse = accountFeignClient.getAccountByEmail(email);
                //     if (accountDtoResponse.getContent().size() != 0) {
                //         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
                //     } else if(accountDtoResponse.getContent().size() != 1) {
                //         log.error("two or more accounts found with same email: {}", email);
                //         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
                //     }
                // } catch (FeignException e) {
                //     throw new ResponseStatusException(HttpStatus.valueOf(e.status()), e.getMessage());
                // }

                setUserAuthentication(request, id, email, roles);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void setUserAuthentication(HttpServletRequest request, UUID id, String email, List<String> roles) {
        UserDetails userDetails = UserDetailsImpl.build(id, email, roles);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
