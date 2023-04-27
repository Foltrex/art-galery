package com.scnsoft.user.config;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.util.SecurityUtil;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignConfig {

    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    @LoadBalanced
    @Bean
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

    // Test to include JWT token in feign client
    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authorization = request.getHeader("Authorization");
                log.info(authorization);
                if (StringUtils.isNotBlank(authorization)) {
                    requestTemplate.header("Authorization", authorization);
                } else {
                    UUID id = SecurityUtil.getCurrentAccountId();
                    if (id != null) {
                        Account account = accountRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourseNotFoundException("Account not found by id: " + id));

                        String token = jwtUtils.createToken(
                                account.getEmail(),
                                account.getId(),
                                account.getAccountType(),
                                account.getRoles()
                        );

                        requestTemplate.header("Authorization", "Bearer " + token);
                    }
                }
            }
        };
    }
}
