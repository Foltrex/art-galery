package com.scnsoft.art.security;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.feignclient.AccountFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountFeignClient accountFeignClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            ResponseEntity<AccountDto> response = accountFeignClient.getAccountByEmail(email);
            AccountDto accountDto = response.getBody();
            if (accountDto != null) {
                return null;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
            }

        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.status()), e.getMessage());
        }

    }
}
